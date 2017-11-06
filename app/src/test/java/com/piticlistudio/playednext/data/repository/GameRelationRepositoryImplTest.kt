package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.repository.datasource.dao.relation.RelationDaoRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.Observable
import java.util.concurrent.TimeUnit

internal class GameRelationRepositoryImplTest {

    @Nested
    @DisplayName("Given GameRelationRepositoryImpl instance")
    inner class instance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var localImpl: RelationDaoRepositoryImpl

        private var repository: GameRelationRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameRelationRepositoryImpl(localImpl)
        }

        @Nested
        @DisplayName("When we call loadForGameAndPlatform")
        inner class loadForGameAndPlatform {
            val gameId = 10
            val platformId = 10
            val entity = makeGameRelation()
            var result: TestSubscriber<GameRelation>? = null

            @BeforeEach
            fun setup() {
                doAnswer { Flowable.just(entity) }.whenever(localImpl).loadForGameAndPlatform(anyInt(), anyInt())
                result = repository?.loadForGameAndPlatform(gameId, platformId)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun localIsCalled() {
                verify(localImpl).loadForGameAndPlatform(gameId, platformId)
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
            @DisplayName("And there is no result in local repository")
            inner class withoutLocalResult {

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.error(EmptyResultSetException("no results")))
                    result = repository?.loadForGameAndPlatform(gameId, platformId)?.test()
                }

                @Test
                @DisplayName("Then should emit default game relation")
                fun withoutErrors() {
                    assertNotNull(result)
                    result?.apply {
                        assertNoErrors()
                        assertNotComplete()
                        assertValueCount(1)
                        assertValue { it.currentStatus == GameRelationStatus.NONE }
                    }
                }
            }

            @Nested
            @DisplayName("And there is an unknown error in local repository")
            inner class errorEmission {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(localImpl.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.error(error))
                    result = repository?.loadForGameAndPlatform(gameId, platformId)?.test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withoutErrors() {
                    assertNotNull(result)
                    result?.apply {
                        assertNotComplete()
                        assertNoValues()
                        assertError(error)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call save")
        inner class save {

            val data = makeGameRelation()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.save(any())).thenReturn(Completable.complete())
                observer = repository?.save(data)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun savesInLocalRepository() {
                verify(localImpl).save(data)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class loadForGameCalled {

            private val gameId = 100
            var observer: TestSubscriber<List<GameRelation>>? = null
            private val result = listOf(makeGameRelation(), makeGameRelation())

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.loadForGame(anyInt())).thenReturn(Flowable.just(result))
                observer = repository?.loadForGame(gameId)?.test()
            }

            @Test
            @DisplayName("Then should request local repository")
            fun requestsRepository() {
                verify(localImpl).loadForGame(gameId)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(1)
                    assertValue(result)
                }
            }

        }
    }
}