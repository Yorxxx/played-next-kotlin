package com.piticlistudio.playednext.domain.interactor.relation

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformUseCase
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class LoadGameRelationUseCaseTest {

    @Nested
    @DisplayName("Given a LoadGameRelationUseCase instance")
    inner class instance {

        private lateinit var usecase: LoadGameRelationUseCase
        @Mock private lateinit var loadGameUseCase: LoadGameUseCase
        @Mock private lateinit var loadPlatformUseCase: LoadPlatformUseCase
        @Mock private lateinit var gamerelationRepository: GameRelationRepository

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            usecase = LoadGameRelationUseCase(loadGameUseCase, loadPlatformUseCase, gamerelationRepository)
        }

        @Nested
        @DisplayName("When we call execute")
        inner class executeIsCalled {

            private var observer: TestSubscriber<GameRelation>? = null
            private val gameId = 100
            private val platformId = 1000
            private val game = makeGame()
            private val platform = makePlatform()
            private val gamerelation = makeGameRelation()

            @BeforeEach
            internal fun setUp() {
                whenever(loadGameUseCase.execute(anyInt())).thenReturn(Single.just(game))
                whenever(loadPlatformUseCase.execute(anyInt())).thenReturn(Single.just(platform))
                whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.just(gamerelation))
                observer = usecase.execute(Pair(gameId, platformId)).test()
            }

            @Test
            @DisplayName("Then should request LoadGameUseCase")
            fun shouldRequestGameUseCase() {
                verify(loadGameUseCase).execute(gameId)
            }

            @Test
            @DisplayName("Then should request loadPlatformUseCase")
            fun shouldRequestPlatform() {
                verify(loadPlatformUseCase).execute(platformId)
            }

            @Test
            @DisplayName("Then should request repository")
            fun shouldRequestRepository() {
                verify(gamerelationRepository).loadForGameAndPlatform(gameId, platformId)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                observer?.apply {
                    assertNoErrors()
                    assertComplete()
                    assertValue(gamerelation)
                    assertEquals(game, gamerelation.game)
                    assertEquals(platform, gamerelation.platform)
                }
            }

            @Nested
            @DisplayName("and GameRelationRepository emits error")
            inner class repositoryError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(Flowable.error(error))
                    observer = usecase.execute(Pair(gameId, platformId)).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(error)
                        assertNoValues()
                        assertNotComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and LoadGameUseCase emits error")
            inner class loadGameUseCaseError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(loadGameUseCase.execute(anyInt())).thenReturn(Single.error(error))
                    observer = usecase.execute(Pair(gameId, platformId)).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(error)
                        assertNoValues()
                        assertNotComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and LoadPlatformUseCase emits error")
            inner class loadPlatformUseCaseError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(loadPlatformUseCase.execute(anyInt())).thenReturn(Single.error(error))
                    observer = usecase.execute(Pair(gameId, platformId)).test()
                }

                @Test
                @DisplayName("Then should emit error")
                fun withError() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertError(error)
                        assertNoValues()
                        assertNotComplete()
                    }
                }
            }
        }
    }
}