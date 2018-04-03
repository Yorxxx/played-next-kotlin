package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeGiantbombCompany
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Test cases for [GiantbombCompanyMapper]
 * Created by e-jegi on 3/26/2018.
 */
internal class GiantbombCompanyMapperTest {

    val mapper = GiantbombCompanyMapper()

    @Nested
    @DisplayName("When we call mapFromDataLayer")
    inner class MapFromDataLayer {

        @Test
        fun `then should map into Company with valid name and id`() {

            val data = makeGiantbombCompany()

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