package com.piticlistudio.playednext.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.repository.datasource.dao.image.GameImageDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.image.GameImageDTORepositoryImpl
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GameImagesRepositoryImplTest {

    @Nested
    @DisplayName("Given a GameImagesRepositoryImpl instance")
    inner class Instance {

        private lateinit var repository: GameImagesRepositoryImpl
        @Mock private lateinit var localImpl: GameImageDaoRepositoryImpl
        @Mock private lateinit var remoteImpl: GameImageDTORepositoryImpl

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameImagesRepositoryImpl(localImpl, remoteImpl)
        }

        @Nested
        @DisplayName("When we call loadForGame")
        inner class LoadForGameCalled {

            private val gameId = 2000
            private var observer: TestSubscriber<List<GameImage>>? = null
            private val result = randomListOf { makeGameImage() }

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<List<GameImage>>({ it.onNext(result) }, BackpressureStrategy.MISSING)
                whenever(localImpl.loadForGame(anyInt())).thenReturn(flowable)
                observer = repository.loadForGame(gameId).test()
            }

            @Test
            @DisplayName("Then should request localImpl")
            fun daoIsCalled() {
                verify(localImpl).loadForGame(gameId)
            }

            @Test
            @DisplayName("Then should not request remoteImpl")
            fun remoteImplIsNotCalled() {
                verifyZeroInteractions(remoteImpl)
            }

            @Test
            @DisplayName("Then emits result")
            fun emitsResult() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertNotComplete()
                    assertValueCount(1)
                    assertValue(result)
                }
            }

            @Nested
            @DisplayName("And is not in cache")
            inner class NotInCache {

                @BeforeEach
                internal fun setUp() {
                    val localFlow = Flowable.create<List<GameImage>>({ it.onNext(listOf()) }, BackpressureStrategy.MISSING)
                    whenever(localImpl.loadForGame(anyInt())).thenReturn(localFlow)

                    val remoteFlow = Flowable.create<List<GameImage>>({ it.onNext(result) }, BackpressureStrategy.MISSING)
                    whenever(remoteImpl.loadForGame(anyInt())).thenReturn(remoteFlow)

                    whenever(localImpl.saveForGame(anyInt(), com.nhaarman.mockito_kotlin.any())).thenReturn(Completable.complete())
                    whenever(localImpl.saveForGame(anyInt(), com.nhaarman.mockito_kotlin.any())).thenReturn(Completable.complete())
                    observer = repository.loadForGame(gameId).test()
                }

                @Test
                @DisplayName("Then should request remote")
                fun requestsRemote() {
                    verify(remoteImpl).loadForGame(gameId)
                }

                @Test
                @DisplayName("Then should cache result")
                fun shouldSaveData() {
                    result.forEach {
                        verify(localImpl).saveForGame(gameId, it)
                    }
                }

                @Test
                @DisplayName("Then should emit without errors")
                fun withoutErrors() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoErrors()
                        assertValueCount(1)
                        assertNotComplete()
                        assertValue(result)
                    }
                }

                @Nested
                @DisplayName("And remote fails")
                inner class RemoteFails() {

                    private val error = Throwable("foo")

                    @BeforeEach
                    internal fun setUp() {
                        whenever(remoteImpl.loadForGame(anyInt())).thenReturn(Flowable.error(error))
                        observer = repository.loadForGame(gameId).test()
                    }

                    @Test
                    @DisplayName("Then should emit error")
                    fun emitsError() {
                        assertNotNull(observer)
                        observer?.apply {
                            assertNoValues()
                            assertNotComplete()
                            assertError(error)
                        }
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call saveForGame")
        inner class SaveCalled {

            private val gameId = 1000
            private val data = randomListOf { makeGameImage() }
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                whenever(localImpl.saveForGame(anyInt(), any())).thenReturn(Completable.complete())
                observer = repository.save(gameId, data).test()
            }

            @Test
            @DisplayName("Then should request localImpl")
            fun requestsLocal() {
                data.forEach {
                    verify(localImpl).saveForGame(gameId, it)
                }
            }

            @Test
            @DisplayName("Then should not request remote")
            fun doesNotRequestRemote() {
                verifyZeroInteractions(remoteImpl)
            }

            @Test
            @DisplayName("Then emits result")
            fun emitsResult() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertNoValues()
                }
            }
        }
    }
}