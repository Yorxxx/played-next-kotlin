package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.room.RoomCompany
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.factory.CompanyFactory.Factory.makeCompanyRoom
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RoomCompanyMapperTest {

    @Nested
    @DisplayName("Given a RoomCompanyMapper instance")
    inner class Instance {

        private lateinit var companyMapper: RoomCompanyMapper

        @BeforeEach
        internal fun setUp() {
            companyMapper = RoomCompanyMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val model = makeCompanyRoom()
            private var result: Company? = null

            @BeforeEach
            internal fun setUp() {
                result = companyMapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                assertEquals(model.id, result?.id)
                assertEquals(model.name, result?.name)
                assertEquals(model.url, result?.url)
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDataLayerModel")
        inner class MapIntoDataLayerModelCalled {

            private val entity = makeCompany()
            private var result: RoomCompany? = null

            @BeforeEach
            internal fun setup() {
                result = companyMapper.mapIntoDataLayerModel(entity)
            }

            @Test
            @DisplayName("Then should map entity")
            fun isMapped() {
                assertNotNull(result)
                assertEquals(entity.id, result?.id)
                assertEquals(entity.name, result?.name)
                assertEquals(entity.url, result?.url)
            }
        }
    }
}