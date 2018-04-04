package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.room.RoomPlatform
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeRoomPlatform
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomPlatformMapperTest {

    @Nested
    @DisplayName("Given a RoomPlatformMapper instance")
    inner class Instance {

        private lateinit var mapper: RoomPlatformMapper

        @BeforeEach
        internal fun setUp() {
            mapper = RoomPlatformMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val model = makeRoomPlatform()
            private var result: Platform? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                result?.let {
                    assertEquals(model.id, it.id)
                    assertEquals(model.abbreviation, it.displayName)
                    assertEquals(model.created_at, it.createdAt)
                    assertEquals(model.updated_at, it.updatedAt)
                    assertEquals(model.name, it.name)
                    assertEquals(model.url, it.url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDataLayerModel")
        inner class MapIntoDataLayerModelCalled {

            private val model = makePlatform()
            private var result: RoomPlatform? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapIntoDataLayerModel(model)
            }

            @Test
            @DisplayName("Then should map entity")
            fun isMapped() {
                assertNotNull(result)
                result?.let {
                    assertEquals(model.id, it.id)
                    assertEquals(model.displayName, it.abbreviation)
                    assertEquals(model.createdAt, it.created_at)
                    assertEquals(model.updatedAt, it.updated_at)
                    assertEquals(model.name, it.name)
                    assertEquals(model.url, it.url)
                }
            }
        }
    }
}