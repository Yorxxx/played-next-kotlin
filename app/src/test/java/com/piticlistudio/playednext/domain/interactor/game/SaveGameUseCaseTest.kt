package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

internal class SaveGameUseCaseTest {

    @Nested
    @DisplayName("Given SaveGameUseCase instance")
    inner class SaveGameUseCaseInstance {

        @Mock
        private lateinit var repository: GameRepository
        @Mock
        private lateinit var platformRepository: PlatformRepository

        private var usecase: SaveGameUseCase? = null

        val game = makeGame()

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            whenever(repository.save(game)).thenReturn(Completable.complete())
            whenever(platformRepository.saveForGame(any(), any())).thenReturn(Completable.complete())
            usecase = SaveGameUseCase(repository, platformRepository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class ExecuteIsCalled {

            var observer: TestObserver<Void>? = null

            @Nested
            @DisplayName("and does have developers")
            inner class WithDevelopers {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    with(observer) {
                        this!!.assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does have platforms")
            inner class WithPlatforms {

                @BeforeEach
                internal fun setUp() {
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then saves into repository")
                fun requestsRepository() {
                    verify(repository).save(game)
                }

                @Test
                @DisplayName("Then saves platforms")
                fun savesPlatforms() {
                    verify(platformRepository).saveForGame(game.id, game.platforms!!)
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }

            @Nested
            @DisplayName("and does not have platforms")
            inner class WithoutPlatforms {

                var observer: TestObserver<Void>? = null

                @BeforeEach
                internal fun setUp() {
                    game.platforms = null
                    observer = usecase?.execute(game)?.test()
                }

                @Test
                @DisplayName("Then does not save platforms")
                fun doesNotRequestsRepository() {
                    verify(platformRepository, never()).saveForGame(anyInt(), any())
                }

                @Test
                @DisplayName("Then emits without errors")
                fun emits() {
                    assertNotNull(observer)
                    observer?.apply {
                        assertNoValues()
                        assertNoErrors()
                        assertComplete()
                    }
                }
            }
        }
    }
}