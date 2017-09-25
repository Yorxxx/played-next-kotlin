package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Test cases for [SearchGamesUseCase] class
 * Created by jorge on 22/09/17.
 */
@RunWith(MockitoJUnitRunner::class)
class SearchGamesUseCaseTest {

    @Mock lateinit var repository: GameRepository

    private var useCase: SearchGamesUseCase? = null

    @Before
    fun setUp() {
        useCase = SearchGamesUseCase(repository)
    }

    @Test
    fun shouldRequestGamesToRepository() {
        val response = listOf<Game>()
        Mockito.`when`(repository.search("foo"))
                .thenReturn(Single.just(response))

        val result = useCase?.execute()?.test()

        assertNotNull(result)
        with(result) {
            this?.assertNoErrors()
            this?.assertValueCount(1)
            this?.assertValue(response)
        }
    }
}