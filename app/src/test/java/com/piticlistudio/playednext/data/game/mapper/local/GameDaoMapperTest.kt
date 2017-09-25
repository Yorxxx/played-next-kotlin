package com.piticlistudio.playednext.data.game.mapper.local

import com.piticlistudio.playednext.data.game.model.GameEntity
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameDaoMapperTest {

    @Nested
    @DisplayName("Given a GameDaoMapper instance")
    inner class GameDaoMapperInstance {

        private lateinit var mapper: GameDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GameDaoMapper()
        }

        @Nested
        @DisplayName("When mapFromRemote is called")
        inner class mapFromRemoteCalled {

            val model = LocalGame(0, "name", null, "storyline", 0, 1, 10f)
            var result: GameEntity? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromRemote(model)
            }

            @Test
            @DisplayName("Then should map into GameEntity")
            fun shouldMap() {
                assertNotNull(result)
                with(result) {
                    assertEquals(model.id.toInt(), this!!.id)
                    assertEquals(model.name, name)
                    assertEquals(model.summary, summary)
                    assertEquals(model.storyline, storyline)
                    assertEquals(model.collection, collection)
                    assertEquals(model.franchise, franchise)
                    assertEquals(model.rating, rating)
                }
            }
        }

        @Nested
        @DisplayName("When mapIntoDaoModel is called")
        inner class mapIntoDaoModelCalled {

            val entity = GameEntity(0, "name", "summary", "storyline", 1, 2, 50f)
            var result: LocalGame? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapIntoDaoModel(entity)
            }

            @Test
            @DisplayName("Then should map into Dao")
            fun shouldMap() {
                assertNotNull(result)
                with(result) {
                    assertEquals(entity.id, this!!.id.toInt())
                    assertEquals(entity.name, name)
                    assertEquals(entity.summary, summary)
                    assertEquals(entity.storyline, storyline)
                    assertEquals(entity.collection, collection)
                    assertEquals(entity.franchise, franchise)
                    assertEquals(entity.rating, rating)
                }
            }
        }
    }
}