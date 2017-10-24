package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Test cases for [SearchGamesUseCase] class
 * Created by jorge on 22/09/17.
 */
class SearchGamesUseCaseTest {

    @Nested
    @DisplayName("Given SearchGamesUseCase instance")
    inner class SearchGamesUseCaseInstance {

        @Mock lateinit var repository: GameRepository

        private var useCase: SearchGamesUseCase? = null

        @BeforeEach
        fun setup() {
            MockitoAnnotations.initMocks(this)
            useCase = SearchGamesUseCase(repository)
        }

        @Nested
        @DisplayName("When execute is called")
        inner class execute {

            val games = listOf<Game>()
            var response: TestObserver<List<Game>>? = null

            @BeforeEach
            internal fun setUp() {
                Mockito.`when`(repository.search("foo"))
                        .thenReturn(Single.just(games))

                response = useCase?.execute("foo")?.test()
            }

            @Test
            @DisplayName("Then requests repository")
            fun repositoryIsCalled() {
                verify(repository).search("foo")
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(response)
                with(response) {
                    this!!.assertNoErrors()
                    assertComplete()
                    assertValueCount(1)
                    assertValue(games)
                }
            }
        }

    }
}