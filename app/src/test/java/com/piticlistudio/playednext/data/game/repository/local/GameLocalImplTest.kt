package com.piticlistudio.playednext.data.game.repository.local

import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.data.game.mapper.local.GameDaoMapper
import com.piticlistudio.playednext.data.game.model.GameEntity
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertNotNull

internal class GameLocalImplTest {

    @Nested
    @DisplayName("Given GameLocalImpl instance")
    inner class localImplInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        private lateinit var dao: GameDao
        @Mock
        private lateinit var mapper: GameDaoMapper

        private lateinit var repository: GameLocalImpl

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            repository = GameLocalImpl(dao, mapper)
        }

        @Nested
        @DisplayName("When load is called")
        inner class loadIsCalled {

            private var observer: TestObserver<GameEntity>? = null
            private val model = LocalGame(10, "name", "storyline", "summary")
            private val entity = GameEntity(10, "name", "storyline", "summary", 0, 1, 5f)

            @BeforeEach
            internal fun setUp() {
                Mockito.`when`(dao.findGameById(10))
                        .thenReturn(Single.just(model))
                Mockito.`when`(mapper.mapFromRemote(model)).thenReturn(entity)
                observer = repository.load(10).test()
            }

            @Test
            @DisplayName("Then should request DAO")
            fun daoIsCalled() {
                verify(dao).findGameById(10)
            }

            @Test
            @DisplayName("Then should map dao result")
            fun mapIsCalled() {
                verify(mapper).mapFromRemote(model)
            }

            @Test
            @DisplayName("Then should emit without errors")
            fun withoutErrors() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertComplete()
                    assertValueCount(1)
                    assertNoErrors()
                    assertValue(entity)
                }
            }
        }

        @Nested
        @DisplayName("When save is called")
        inner class saveIsCalled {

            private val entity = GameEntity(10, "name", "storyline", "summary", 0, 1, 5f)
            private val model = LocalGame(10, "name", "storyline", "summary")
            private var observer: TestObserver<Void>? = null

            @BeforeEach
            internal fun setUp() {
                Mockito.`when`(mapper.mapIntoDaoModel(entity)).thenReturn(model)
                observer = repository.save(entity).test()
            }

            @Test
            @DisplayName("Then maps entity into Dao model")
            fun mapsEntity() {
                verify(mapper).mapIntoDaoModel(entity)
            }

            @Test
            @DisplayName("Then inserts into DAO")
            fun isInserted() {
                verify(dao).insertGame(model)
            }

            @Test
            @DisplayName("Then emits completion")
            fun emitsComplete() {
                assertNotNull(observer)
                with(observer) {
                    this!!.assertComplete()
                    assertNoValues()
                    assertNoErrors()
                }
            }
        }
    }
}