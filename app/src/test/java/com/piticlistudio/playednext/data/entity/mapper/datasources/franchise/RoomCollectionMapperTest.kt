package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.data.entity.room.RoomCollection
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeRoomCollection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomCollectionMapperTest {

    @Nested
    @DisplayName("Given a RoomCollectionMapper instance")
    inner class Instance {

        private lateinit var mapper: RoomCollectionMapper

        @BeforeEach
        internal fun setUp() {
            mapper = RoomCollectionMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val model = makeRoomCollection()
            private var result: Collection? = null

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
        inner class MapIntoDataLayerModelCalled {

            private val entity = makeCollection()
            private var result: RoomCollection? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapIntoDataLayerModel(entity)
            }

            @Test
            @DisplayName("Then should map model")
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