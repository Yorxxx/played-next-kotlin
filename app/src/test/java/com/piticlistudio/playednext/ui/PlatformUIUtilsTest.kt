package com.piticlistudio.playednext.ui

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlatformUIUtilsTest {

    @Nested
    @DisplayName("Given PlatformUIUtils instance")
    inner class Instance {

        private lateinit var utils: PlatformUIUtils
        private var colorsMap = HashMap<String, Int>()
        var acronymsMap = HashMap<String, String>()

        @Nested
        @DisplayName("When we call getAcronym")
        inner class GetAcronymCalled {

            @Test
            @DisplayName("Then should return acronym value from map")
            fun acronymFromMap() {

                acronymsMap.put("viruta", "vrt")
                utils = PlatformUIUtils(colorsMap, acronymsMap)
                val result = utils.getAcronym("viruta")
                assertEquals("vrt", result)
            }

            @Test
            @DisplayName("Then should return input value if is not mapped")
            fun acronymFromInput() {

                utils = PlatformUIUtils(colorsMap, acronymsMap)
                val result = utils.getAcronym("viruta")
                assertEquals("viruta", result)
            }
        }
    }
}