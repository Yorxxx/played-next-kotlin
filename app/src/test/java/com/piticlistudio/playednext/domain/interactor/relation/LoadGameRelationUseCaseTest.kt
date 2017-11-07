package com.piticlistudio.playednext.domain.interactor.relation

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Assertions.assertNotNull
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
    inner class Instance {

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
        inner class ExecuteIsCalled {

            private var observer: TestSubscriber<GameRelation>? = null
            private val gameId = 100
            private val platformId = 1000
            private val game = makeGame()
            private val platform = makePlatform()
            private val gamerelation = makeGameRelation()
            private val gamerelation2 = makeGameRelation()

            @BeforeEach
            internal fun setUp() {
                val flowable = Flowable.create<Game>({
                    it.onNext(game)
                }, BackpressureStrategy.MISSING)
                whenever(loadGameUseCase.execute(anyInt())).thenReturn(flowable)
                whenever(loadPlatformUseCase.execute(anyInt())).thenReturn(Single.just(platform))

                val relFlow = Flowable.create<GameRelation>({
                    it.onNext(gamerelation)
                    it.onNext(gamerelation2)
                }, BackpressureStrategy.MISSING)
                whenever(gamerelationRepository.loadForGameAndPlatform(anyInt(), anyInt())).thenReturn(relFlow)
                observer = usecase.execute(Pair(gameId, platformId)).test()
            }

            @Test
            @DisplayName("Then should request LoadGameUseCase")
            fun shouldRequestGameUseCase() {
                verify(loadGameUseCase, times(2)).execute(gameId)
            }

            @Test
            @DisplayName("Then should request loadPlatformUseCase")
            fun shouldRequestPlatform() {
                verify(loadPlatformUseCase, times(2)).execute(platformId)
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
                    assertNotComplete()
                    assertValueCount(2)
                    assertValueAt(0, {
                        it == gamerelation && it.platform == platform && it.game == game
                    })
                    assertValueAt(1, {
                        it == gamerelation2 && it.platform == platform && it.game == game
                    })
                }
            }

            @Nested
            @DisplayName("and GameRelationRepository emits error")
            inner class RepositoryError {

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
            inner class LoadGameUseCaseError {

                private val error = Throwable("foo")

                @BeforeEach
                internal fun setUp() {
                    whenever(loadGameUseCase.execute(anyInt())).thenReturn(Flowable.error(error))
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
            inner class LoadPlatformUseCaseError {

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