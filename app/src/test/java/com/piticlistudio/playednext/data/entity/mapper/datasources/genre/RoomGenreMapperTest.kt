package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.data.entity.room.RoomGenre
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeRoomGenre
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomGenreMapperTest {

    @Nested
    @DisplayName("Given a RoomGenreMapper instance")
    inner class Instance {

        private lateinit var mapper: RoomGenreMapper

        @BeforeEach
        internal fun setUp() {
            mapper = RoomGenreMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromModelCalled {

            private val model = makeRoomGenre()
            private var result: Genre? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.url, url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDataLayerModel")
        inner class MapFromEntityCalled {

            private val entity = makeGenre()
            private var result: RoomGenre? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapIntoDataLayerModel(entity)
            }

            @Test
            @DisplayName("Then should map entity")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.id, id)
                    assertEquals(entity.name, name)
                    assertEquals(entity.url, url)
                }
            }
        }
    }
}