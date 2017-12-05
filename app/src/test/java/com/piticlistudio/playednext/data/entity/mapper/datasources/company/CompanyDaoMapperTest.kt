package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDaoMapper
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompanyDaoMapperTest {

    @Nested
    @DisplayName("Given a CompanyDaoMapper instance")
    inner class Instance {

        private lateinit var mapper: CompanyDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CompanyDaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val model = makeCompanyDao()
            private var result: Company? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.slug, slug)
                    assertEquals(model.url, url)
                    assertEquals(model.created_at, createdAt)
                    assertEquals(model.updated_at, updatedAt)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntityCalled {

            private val entity = makeCompany()
            private var result: CompanyDao? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapFromEntity(entity)
            }

            @Test
            @DisplayName("Then should map entity")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.id, id)
                    assertEquals(entity.name, name)
                    assertEquals(entity.slug, slug)
                    assertEquals(entity.url, url)
                    assertEquals(entity.createdAt, created_at)
                    assertEquals(entity.updatedAt, updated_at)
                }
            }
        }
    }
}