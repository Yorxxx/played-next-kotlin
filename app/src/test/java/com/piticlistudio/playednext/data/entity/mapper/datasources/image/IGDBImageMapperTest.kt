package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.domain.model.Image
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeIGDBImage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class IGDBImageMapperTest {

    @Nested
    @DisplayName("Given a IGDBImageMapper instance")
    inner class IGDBImageMapperInstance {

        private lateinit var mapper: IGDBImageMapper

        @BeforeEach
        internal fun setUp() {
            mapper = IGDBImageMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val model = makeIGDBImage()
            private var result: Image? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map result")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.height, height)
                    assertEquals(model.width, width)
                    assertEquals(model.mediumSizeUrl, url)
                }
            }
        }
    }
}