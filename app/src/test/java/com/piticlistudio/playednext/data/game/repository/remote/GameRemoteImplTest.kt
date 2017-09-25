package com.piticlistudio.playednext.data.game.repository.remote

import com.piticlistudio.playednext.data.game.mapper.remote.IGDBGameMapper
import com.piticlistudio.playednext.data.game.model.GameModel
import com.piticlistudio.playednext.data.game.model.remote.IGDBGameModel
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertNotNull

@RunWith(MockitoJUnitRunner::class)
class GameRemoteImplTest {

    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock lateinit var service: GameService
    @Mock lateinit var mapper: IGDBGameMapper

    private var repositoryImpl: GameRemoteImpl? = null

    @Before
    fun setUp() {
        repositoryImpl = GameRemoteImpl(service, mapper)
    }

    @Test
    fun Given_EmptyList_When_Load_Then_ThrowsError() {
        `when`(service.load(10, "*"))
                .thenReturn(Single.just(listOf()))

        val result = repositoryImpl?.load(10)?.test()

        with(result) {
            this?.assertNoValues()
            this?.assertNotComplete()
            this?.assertError(Throwable::class.java)
        }
        verify(service).load(10, "*")
    }

    @Test
    fun Given_MultipleList_When_Load_Then_ThrowsError() {
        val response = listOf(IGDBGameModel(10, "name", "summary", "story", 0, 1, 2),
                IGDBGameModel(10, "name2", "summary2", "story2", 0, 1, 2))

        `when`(service.load(10, "*"))
                .thenReturn(Single.just(response))

        val result = repositoryImpl?.load(10)?.test()

        verify(service).load(10, "*")
        with(result) {
            this?.assertNoValues()
            this?.assertNotComplete()
            this?.assertError(Throwable::class.java)
        }
    }

    @Test
    fun Given_ValidReponse_When_Load_Then_EmitsGame() {
        val model = IGDBGameModel(10, "name", "summary", "story", 0, 1, 2)
        val response = listOf(model)
        val entity = GameModel(10, "name", "summary", "storyline", 0, 1, 2)

        `when`(service.load(10, "*"))
                .thenReturn(Single.just(response))
        `when`(mapper.mapFromRemote(model)).thenReturn(entity)

        val result = repositoryImpl?.load(10)?.test()

        verify(service).load(10, "*")
        verify(mapper).mapFromRemote(model)
        with(result) {
            this?.assertValueCount(1)
            this?.assertComplete()
            this?.assertNoErrors()
            this?.assertValue(entity)
        }
    }

    @Test
    fun shouldRequestServiceWithCorrectParamsWhenSearching() {

        `when`(service.search(0, "query", "*", 20))
                .thenReturn(Single.just(listOf()))

        repositoryImpl?.search("query")?.test()

        verify(service).search(0, "query", "*", 20)
    }

    @Test
    fun shouldMapSearchResultIntoDataModels() {
        val model = IGDBGameModel(10, "name", "summary", "story", 0, 1, 2)
        val model2 = IGDBGameModel(11, "name2", "summary2", "story2", 1, 2, 3)
        val response = listOf(model, model2)

        `when`(service.search(0, "query", "*", 20))
                .thenReturn(Single.just(response))
        `when`(mapper.mapFromRemote(model))
                .thenReturn(GameModel(10, "name", "summary", "story", 1, 2, 3))

        repositoryImpl?.search("query")?.test()

        verify(mapper).mapFromRemote(model)
        verify(mapper).mapFromRemote(model2)
    }

    @Test
    fun shouldEmitSearchResultsWithoutErrors() {
        val model = IGDBGameModel(10, "name", "summary", "story", 0, 1, 2)
        val model2 = IGDBGameModel(11, "name2", "summary2", "story2", 1, 2, 3)
        val response = listOf(model, model2)
        val entity1 = GameModel(10, "name", "summary", "story", 1, 2, 3)
        val entity2 = GameModel(10, "name", "summary", "story", 1, 2, 3)

        `when`(service.search(0, "query", "*", 20))
                .thenReturn(Single.just(response))
        `when`(mapper.mapFromRemote(model))
                .thenReturn(entity1)
        `when`(mapper.mapFromRemote(model2))
                .thenReturn(entity2)

        // Act
        val result = repositoryImpl?.search("query")?.test()

        assertNotNull(result)
        with(result) {
            this?.assertValueCount(1)
            this?.assertComplete()
            this?.assertNoErrors()
            this?.assertValue(listOf(entity1, entity2))
        }
    }
}