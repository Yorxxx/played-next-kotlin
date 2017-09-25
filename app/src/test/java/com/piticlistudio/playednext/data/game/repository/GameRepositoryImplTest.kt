package com.piticlistudio.playednext.data.game.repository

import com.piticlistudio.playednext.data.game.mapper.GameEntityMapper
import com.piticlistudio.playednext.data.game.model.GameModel
import com.piticlistudio.playednext.data.game.repository.remote.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Test cases for [GameRepositoryImpl]
 */
class GameRepositoryImplTest {


    @Nested
    @DisplayName("Given a GameRepository instance")
    inner class GameRepositoryImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var remoteImpl: GameRemoteImpl
        @Mock
        private lateinit var mapper: GameEntityMapper

        private var repository: GameRepositoryImpl? = null

        @BeforeEach
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameRepositoryImpl(remoteImpl, mapper)
        }

        @Nested
        @DisplayName("When we call load")
        inner class Load {
            val response = GameModel(10, "name", "summary", "storyline", 1, 2, 3)
            val entity = Game(10, "name", "summary", "storyline")
            var result: TestObserver<Game>? = null

            @BeforeEach
            fun setup() {
                Mockito.`when`(remoteImpl.load(10))
                        .thenReturn(Single.just(response))
                Mockito.`when`(mapper.mapFromRemote(response))
                        .thenReturn(entity)
                result = repository?.load(10)?.test()
            }

            @Test
            @DisplayName("Then should request remote repository")
            fun remoteIsCalled() {
                verify(remoteImpl).load(10)
            }

            @Test
            @DisplayName("Then should map emission into domain model")
            fun isMapped() {
                verify(mapper).mapFromRemote(response)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(result)
                with(result) {
                    this?.assertNoErrors()
                    this?.assertValueCount(1)
                    this?.assertComplete()
                    this?.assertValue(entity)
                }
            }

        }

        @Nested
        @DisplayName("When we call search")
        inner class Search {

            val model1 = GameModel(10, "name", "summary", "storyline", 1, 2, 3)
            val model2 = GameModel(10, "name", "summary", "storyline", 1, 2, 3)
            var result: TestObserver<List<Game>>? = null
            val entity = Game(10, "name", "summary", "storyline")
            val entity2 = Game(10, "name", "summary", "storyline")

            @BeforeEach
            fun setup() {
                Mockito.`when`(remoteImpl.search("query")).thenReturn(Single.just(listOf(model1, model2)))
                Mockito.`when`(mapper.mapFromRemote(model1)).thenReturn(entity)
                Mockito.`when`(mapper.mapFromRemote(model2)).thenReturn(entity2)
                result = repository?.search("query")?.test()
            }

            @Test
            @DisplayName("Then should request remote implementation")
            fun remoteIsCalled() {
                verify(remoteImpl).search("query")
            }

            @Test
            @DisplayName("Then should map returned results")
            fun mapIsCalled() {
                verify(mapper).mapFromRemote(model1)
                verify(mapper).mapFromRemote(model2)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun emissionWithoutErrors() {
                assertNotNull(result)
                with(result) {
                    this?.assertNoErrors()
                    this?.assertComplete()
                    this?.assertValueCount(1)
                    this?.assertValue(listOf(entity, entity2))
                }
            }
        }
    }
}