package com.piticlistudio.playednext.data.repository

import android.app.AlarmManager
import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
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
        private lateinit var remoteImpl: GameRemoteImpl
        @Mock
        private lateinit var localImpl: GameLocalImpl

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
            var result: TestObserver<Game>? = null

            @BeforeEach
            fun setup() {
                whenever(localImpl.load(anyInt())).thenReturn(Single.just(entity))
                whenever(localImpl.save(entity)).thenReturn(Completable.complete())
                whenever(remoteImpl.load(anyInt())).thenReturn(Single.just(entity))
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
                    assertComplete()
                    assertValue(entity)
                }
            }

            @Nested
            @DisplayName("And local repository has unsynced data")
            inner class localDataIsOld {

                @BeforeEach
                internal fun setUp() {
                    entity.syncedAt = System.currentTimeMillis() - AlarmManager.INTERVAL_DAY * 15
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
                    with(result) {
                        this?.assertNoErrors()
                        this?.assertValueCount(1)
                        this?.assertComplete()
                        this?.assertValue(entity)
                    }
                }

                @Test
                @DisplayName("Then should cache retrieved data")
                fun cacheResponse() {
                    verify(localImpl).save(entity)
                }

                @Nested
                @DisplayName("And request fails")
                inner class refreshFailed {

                    @BeforeEach
                    internal fun setUp() {
                        whenever(remoteImpl.load(anyInt())).thenReturn(Single.error(Throwable("No Internet Connection")))
                        result = repository?.load(id)?.test()
                    }

                    @Test
                    @DisplayName("Then should emit without errors")
                    fun withoutErrors() {
                        assertNotNull(result)
                        with(result) {
                            this?.assertNoErrors()
                            this?.assertValueCount(1)
                            this?.assertComplete()
                            this?.assertValue(entity)
                        }
                    }
                }
            }

            @Nested
            @DisplayName("And there is no result in local repository")
            inner class withoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.load(id)).thenReturn(Single.error(EmptyResultSetException("no results")))
                    whenever(remoteImpl.load(id)).thenReturn(Single.just(entity))
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
                    with(result) {
                        this?.assertNoErrors()
                        this?.assertValueCount(1)
                        this?.assertComplete()
                        this?.assertValue(entity)
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

            val model1 = makeGame()
            val model2 = makeGame()
            var result: TestObserver<List<Game>>? = null

            @BeforeEach
            fun setup() {
                whenever(remoteImpl.search("query")).thenReturn(Single.just(listOf(model1, model2)))
                result = repository?.search("query")?.test()
            }

            @Test
            @DisplayName("Then should request remote implementation")
            fun remoteIsCalled() {
                verify(remoteImpl).search("query")
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun emissionWithoutErrors() {
                assertNotNull(result)
                with(result) {
                    this?.assertNoErrors()
                    this?.assertComplete()
                    this?.assertValueCount(1)
                    this?.assertValue(listOf(model1, model2))
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