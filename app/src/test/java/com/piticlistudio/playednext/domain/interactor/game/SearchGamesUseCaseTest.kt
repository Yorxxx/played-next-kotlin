package com.piticlistudio.playednext.domain.interactor.game

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
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

            private val games = listOf(makeGame(), makeGame())
            private var response: TestSubscriber<List<Game>>? = null
            private val query = "mario"
            private val offset = 2
            private val limit = 8

            @BeforeEach
            internal fun setUp() {
                val flow = Flowable.create<List<Game>>({ it.onNext(games) }, BackpressureStrategy.MISSING)
                whenever(repository.search(anyString(), anyInt(), anyInt())).thenReturn(flow)
                response = useCase?.execute(SearchQuery(query, offset, limit))?.test()
            }

            @Test
            @DisplayName("Then requests repository")
            fun repositoryIsCalled() {
                verify(repository).search(query, offset, limit)
            }

            @Test
            @DisplayName("Then emits without errors")
            fun withoutErrors() {
                assertNotNull(response)
                response?.apply {
                    assertNoErrors()
                    assertValueCount(1)
                    assertNotComplete()
                    assertValue(games)
                }
            }
        }
    }
}