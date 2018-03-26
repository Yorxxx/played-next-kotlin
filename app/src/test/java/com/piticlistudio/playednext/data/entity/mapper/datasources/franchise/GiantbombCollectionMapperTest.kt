package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGiantbombFranchise
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Test cases for [GiantbombCollectionMapper]
 * Created by e-jegi on 3/26/2018.
 */
internal class GiantbombCollectionMapperTest {

    val mapper = GiantbombCollectionMapper()

    @Nested
    @DisplayName("When we call mapFromDataLayer")
    inner class MapFromDataLayer {

        @Test
        fun `then should map into Collection`() {

            val data = makeGiantbombFranchise()

            val result = mapper.mapFromDataLayer(data)

            assertNotNull(result)
            with(result) {
                assertEquals(data.name, result.name)
                assertEquals(data.id, result.id)
                assertEquals(data.site_detail_url, result.url)
            }
        }
    }
}