package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeGiantbombPlatform
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Test methods for [GiantbombPlatformMapper]
 * Created by e-jegi on 3/26/2018.
 */
internal class GiantbombPlatformMapperTest {

    val mapper = GiantbombPlatformMapper()

    @Nested
    @DisplayName("When we call mapFromDataLayer")
    inner class MapFromDataLayer {

        @Test
        fun `then should map into Platform with valid name and id`() {

            val data = makeGiantbombPlatform()

            val result = mapper.mapFromDataLayer(data)

            assertNotNull(result)
            with(result) {
                assertEquals(data.name, result.name)
                assertEquals(data.id, result.id)
                assertEquals(data.abbreviation, result.displayName)
                assertEquals(data.site_detail_url, result.url)
                assertEquals(0, result.createdAt)
                assertEquals(0, result.updatedAt)
            }
        }
    }
}