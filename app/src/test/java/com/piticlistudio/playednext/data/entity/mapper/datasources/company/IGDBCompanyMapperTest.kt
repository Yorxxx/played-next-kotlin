package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.factory.CompanyFactory.Factory.makeIGDBCompany
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class IGDBCompanyMapperTest {

    @Nested
    @DisplayName("Given IGDBCompanyMapper instance")
    inner class MapperInstance {

        private lateinit var mapperIGDB: IGDBCompanyMapper

        @BeforeEach
        internal fun setUp() {
            mapperIGDB = IGDBCompanyMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromModelCalled {

            private val source = makeIGDBCompany()
            var result: Company? = null

            @BeforeEach
            internal fun setUp() {
                result = mapperIGDB.mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should map into company list")
            fun mapped() {
                assertNotNull(result)
                assertEquals(source.id, result?.id)
                assertEquals(source.name, result?.name)
                assertEquals(source.url, result?.url)
            }
        }
    }
}