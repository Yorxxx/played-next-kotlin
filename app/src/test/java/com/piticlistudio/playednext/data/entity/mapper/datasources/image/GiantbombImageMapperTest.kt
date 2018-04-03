package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.net.GiantbombGameImage
import com.piticlistudio.playednext.test.factory.DataFactory
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGiantbombGameImage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Test cases for [GiantbombImageMapper]
 * Created by e-jegi on 3/26/2018.
 */
internal class GiantbombImageMapperTest {

    private val mapper = GiantbombImageMapper()

    @Nested
    @DisplayName("When we call mapFromDataLayer")
    inner class MapFromModel {

        @Test
        fun `then should map into GameImage with medium url values`() {

            val response = makeGiantbombGameImage()

            val result = mapper.mapFromDataLayer(response)

            assertNotNull(result)
            with(result) {
                assertEquals(response.medium_url, result?.url)
                assertEquals(response.medium_url, result?.id)
            }
        }

        @Test
        fun `then should map into GameImage with screen_url value when medium_url is null`() {

            val response = GiantbombGameImage(icon_url = DataFactory.randomString(),
                    medium_url = null,
                    original_url = DataFactory.randomString(),
                    screen_large_url = DataFactory.randomString(),
                    screen_url = DataFactory.randomString(),
                    small_url = DataFactory.randomString(),
                    super_url = DataFactory.randomString(),
                    thumb_url = DataFactory.randomString(),
                    tiny_url = DataFactory.randomString())

            val result = mapper.mapFromDataLayer(response)

            assertNotNull(result)
            with(result) {
                assertEquals(response.screen_url, result?.url)
                assertEquals(response.screen_url, result?.id)
            }
        }

        @Test
        fun `then should map into GameImage with screen_large_url value when medium_url and screen_url are null`() {

            val response = GiantbombGameImage(icon_url = DataFactory.randomString(),
                    medium_url = null,
                    original_url = DataFactory.randomString(),
                    screen_large_url = DataFactory.randomString(),
                    screen_url = null,
                    small_url = DataFactory.randomString(),
                    super_url = DataFactory.randomString(),
                    thumb_url = DataFactory.randomString(),
                    tiny_url = DataFactory.randomString())

            val result = mapper.mapFromDataLayer(response)

            assertNotNull(result)
            with(result) {
                assertEquals(response.screen_large_url, result?.url)
                assertEquals(response.screen_large_url, result?.id)
            }
        }

        @Test
        fun `then should map into GameImage with small_url value when medium_url, screen_url and screen_large_url are null`() {

            val response = GiantbombGameImage(icon_url = DataFactory.randomString(),
                    medium_url = null,
                    original_url = DataFactory.randomString(),
                    screen_large_url = null,
                    screen_url = null,
                    small_url = DataFactory.randomString(),
                    super_url = DataFactory.randomString(),
                    thumb_url = DataFactory.randomString(),
                    tiny_url = DataFactory.randomString())

            val result = mapper.mapFromDataLayer(response)

            assertNotNull(result)
            with(result) {
                assertEquals(response.small_url, result?.url)
                assertEquals(response.small_url, result?.id)
            }
        }

        @Test
        fun `then should map into GameImage with original_url value when medium_url, screen_url, screen_large_url and small_url are null`() {

            val response = GiantbombGameImage(icon_url = DataFactory.randomString(),
                    medium_url = null,
                    original_url = DataFactory.randomString(),
                    screen_large_url = null,
                    screen_url = null,
                    small_url = null,
                    super_url = DataFactory.randomString(),
                    thumb_url = DataFactory.randomString(),
                    tiny_url = DataFactory.randomString())

            val result = mapper.mapFromDataLayer(response)

            assertNotNull(result)
            with(result) {
                assertEquals(response.original_url, result?.url)
                assertEquals(response.original_url, result?.id)
            }
        }

        @Test
        fun `then should return null if no available url to map from`() {

            val response = GiantbombGameImage(icon_url = DataFactory.randomString(),
                    medium_url = null,
                    original_url = null,
                    screen_large_url = null,
                    screen_url = null,
                    small_url = null,
                    super_url = DataFactory.randomString(),
                    thumb_url = DataFactory.randomString(),
                    tiny_url = DataFactory.randomString())

            val result = mapper.mapFromDataLayer(response)

            assertNull(result)
        }

    }
}