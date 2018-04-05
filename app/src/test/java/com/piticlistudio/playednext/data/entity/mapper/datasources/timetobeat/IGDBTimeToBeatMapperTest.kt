package com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat

import com.piticlistudio.playednext.domain.model.TimeToBeat
import com.piticlistudio.playednext.test.factory.TimeToBeatFactory.Factory.makeIGDBTimeToBeat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class IGDBTimeToBeatMapperTest {

    @Nested
    @DisplayName("Given IGDBTimeToBeatMapper instance")
    inner class MapperInstance {

        private lateinit var mapperIGDB: IGDBTimeToBeatMapper

        @BeforeEach
        internal fun setUp() {
            mapperIGDB = IGDBTimeToBeatMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromModelCalled {

            private val source = makeIGDBTimeToBeat()
            var result: TimeToBeat? = null

            @BeforeEach
            internal fun setUp() {
                result = mapperIGDB.mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should map into TimeToBeat")
            fun mapped() {
                assertNotNull(result)
                result?.let {
                    assertEquals(source.completely, it.completely)
                    assertEquals(source.hastly, it.quick)
                    assertEquals(source.normally, it.normally)
                }
            }
        }
    }
}