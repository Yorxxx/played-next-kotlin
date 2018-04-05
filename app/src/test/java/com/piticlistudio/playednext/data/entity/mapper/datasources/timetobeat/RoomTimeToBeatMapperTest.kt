package com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat

import com.piticlistudio.playednext.data.entity.room.RoomTimeToBeat
import com.piticlistudio.playednext.domain.model.TimeToBeat
import com.piticlistudio.playednext.test.factory.TimeToBeatFactory.Factory.makeRoomTimeToBeat
import com.piticlistudio.playednext.test.factory.TimeToBeatFactory.Factory.makeTimeToBeat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomTimeToBeatMapperTest {

    @Nested
    @DisplayName("Given a RoomTimeToBeatMapper instance")
    inner class Instance {

        private lateinit var mapper: RoomTimeToBeatMapper

        @BeforeEach
        internal fun setUp() {
            mapper = RoomTimeToBeatMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val model = makeRoomTimeToBeat()
            private var result: TimeToBeat? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                result?.let {
                    assertEquals(model.completely, it.completely)
                    assertEquals(model.hastly, it.quick)
                    assertEquals(model.normally, it.normally)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDataLayerModel")
        inner class MapIntoDataLayerModelCalled {

            private val model = makeTimeToBeat()
            private var result: RoomTimeToBeat? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapIntoDataLayerModel(model)
            }

            @Test
            @DisplayName("Then should map entity")
            fun isMapped() {
                assertNotNull(result)
                result?.let {
                    assertEquals(model.completely, it.completely)
                    assertEquals(model.quick, it.hastly)
                    assertEquals(model.normally, it.normally)
                }
            }
        }
    }
}