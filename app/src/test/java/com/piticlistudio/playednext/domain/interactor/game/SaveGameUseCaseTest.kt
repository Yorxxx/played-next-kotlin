package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

internal class SaveGameUseCaseTest {

    @Nested
    @DisplayName("Given SaveGameUseCase instance")
    inner class SaveGameUseCaseInstance {

        @Mock
        private lateinit var repository: GameRepository
        private var usecase: SaveGameUseCase? = null

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            usecase = SaveGameUseCase(repository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class executeIsCalled {

            val game = makeGame()
            var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                Mockito.`when`(repository.save(game)).thenReturn(Completable.complete())
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
    }
}