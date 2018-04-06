package com.piticlistudio.playednext.data.repository

import android.app.AlarmManager
import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.igdb.IGDBGameRepositoryImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GameRepositoryImplTest {

    @Nested
    @DisplayName("Given a GameRepository instance")
    inner class GameRepositoryImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var remoteImpl: IGDBGameRepositoryImpl
        @Mock
        private lateinit var localImpl: RoomGameRepositoryImpl

        private var repository: GameRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameRepositoryImpl(remoteImpl, localImpl)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {
            val id = 10
            val entity = makeGame().apply {
                syncedAt = System.currentTimeMillis()
            }
            var result: TestSubscriber<Game>? = null

            @BeforeEach
            fun setup() {
                val flowable = Flowable.create<Game>({ it.onNext(entity) }, BackpressureStrategy.MISSING)
                whenever(localImpl.load(anyInt())).thenReturn(flowable)
                whenever(localImpl.save(entity)).thenReturn(Completable.complete())
                whenever(remoteImpl.load(anyInt())).thenReturn(flowable)
                result = repository?.load(id)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).load(id)
            }

            @Test
            @DisplayName("Then should not request remote repository")
            fun remoteIsNotCalled() {
                verifyZeroInteractions(remoteImpl)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                result?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertNotComplete()
                    assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And local repository has unsynced data")
            inner class LocalDataIsOld {

                @BeforeEach
                internal fun setUp() {
                    entity.syncedAt = System.currentTimeMillis() - AlarmManager.INTERVAL_DAY * 30
                    result = repository?.load(id)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).load(id)
                }

                @Test
                @DisplayName("Then should emit without errors")
                fun withoutErrors() {
                    assertNotNull(result)
                    result?.apply {
                        assertNoErrors()
                        assertValueCount(1)
                        assertNotComplete()
                        assertValue(entity)
                    }
                }

                @Test
                @DisplayName("Then should cache retrieved data")
                fun cacheResponse() {
                    verify(localImpl).save(entity)
                }

                @Nested
                @DisplayName("And request fails")
                inner class RefreshFailed {

                    @BeforeEach
                    internal fun setUp() {
                        val flowable = Flowable.create<Game>({ it.onError(Throwable("No Internet connection")) }, BackpressureStrategy.MISSING)
                        whenever(remoteImpl.load(anyInt())).thenReturn(flowable)
                        result = repository?.load(id)?.test()
                    }

                    @Test
                    @DisplayName("Then should emit without errors")
                    fun withoutErrors() {
                        assertNotNull(result)
                        result?.apply {
                            assertNoErrors()
                            assertValueCount(1)
                            assertValue(entity)
                            assertNotComplete()
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And there is no result in local repository")
            inner class WithoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    val localFlow = Flowable.create<Game>({ it.onError(EmptyResultSetException("No results")) }, BackpressureStrategy.MISSING)
                    whenever(localImpl.load(id)).thenReturn(localFlow)

                    val remoteFlow = Flowable.create<Game>({ it.onNext(entity) }, BackpressureStrategy.MISSING)
                    whenever(remoteImpl.load(id)).thenReturn(remoteFlow)
                    whenever(localImpl.save(entity)).thenReturn(Completable.complete())
                    result = repository?.load(id)?.test()
                }

                @Test
                @DisplayName("Then should request remote repository")
                fun remoteIsCalled() {
                    verify(remoteImpl).load(id)
                }

                @Test
                @DisplayName("Then should emit without errors")
                fun withoutErrors() {
                    assertNotNull(result)
                    result?.apply {
                        assertNoErrors()
                        assertValueCount(1)
                        assertComplete()
                        assertValue(entity)
                    }
                }

                @Test
                @DisplayName("Then should cache retrieved data")
                fun cacheResponse() {
                    verify(localImpl).save(entity)
                }
            }

        }

        @Nested
        @DisplayName("When we call search")
        inner class Search {

            private val model1 = makeGame()
            private val model2 = makeGame()
            var result: TestSubscriber<List<Game>>? = null
            private val query = "foo"
            private val offset = 0
            private val limit = 20

            @BeforeEach
            fun setup() {
                val flow = Flowable.create<List<Game>>({ it.onNext(listOf(model1, model2)) }, BackpressureStrategy.MISSING)
                whenever(remoteImpl.search(anyString(), anyInt(), anyInt())).thenReturn(flow)
                result = repository?.search(query, offset, limit)?.test()
            }

            @Test
            @DisplayName("Then should not request local")
            fun localIsNotCalled() {
                verifyZeroInteractions(localImpl)
            }

            @Test
            @DisplayName("Then should request remote implementation")
            fun remoteIsCalled() {
                verify(remoteImpl).search(query, offset, limit)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun emissionWithoutErrors() {
                assertNotNull(result)
                result?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertNoErrors()
                    assertValue(listOf(model1, model2))
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class Save {

            val entity = makeGame()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.save(entity)).thenReturn(Completable.complete())
                observer = repository?.save(entity)?.test()
            }

            @Test
            @DisplayName("Then saves data model")
            fun dataIsSaved() {
                verify(localImpl).save(entity)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }
        }
    }
}