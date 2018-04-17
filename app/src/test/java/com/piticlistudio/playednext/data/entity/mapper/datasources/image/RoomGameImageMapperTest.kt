package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.room.RoomGameImage
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeRoomGameImage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

internal class RoomGameImageMapperTest {

    @Nested
    @DisplayName("Given a RoomGameImageMapper instance")
    inner class RoomGameImageMapperInstance {

        private lateinit var mapper: RoomGameImageMapper

        @BeforeEach
        internal fun setUp() {
            mapper = RoomGameImageMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val model = makeRoomGameImage()
            private var result: GameImage? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.image.height, height)
                    assertEquals(model.image.width, width)
                    assertEquals(model.image.url, url)
                    assertEquals(model.gameId, gameId)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDataLayerModel")
        inner class MapIntoDataLayerModelCalled {

            private val entity = makeGameImage()
            private var result: RoomGameImage? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapIntoDataLayerModel(entity)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.height, image.height)
                    assertNull(id)
                    assertEquals(entity.url, image.url)
                    assertEquals(entity.width, image.width)
                    assertEquals(entity.gameId, gameId)
                }
            }
        }
    }
}