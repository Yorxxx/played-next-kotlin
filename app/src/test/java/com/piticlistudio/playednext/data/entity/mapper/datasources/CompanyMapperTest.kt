package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDTO
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompanyMapperTest {

    @Nested
    @DisplayName("Given a CompanyMapper.DaoMapper instance")
    inner class CompanyMapperDaoMapperInstance {

        private lateinit var mapper: CompanyMapper.DaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CompanyMapper.DaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDao")
        inner class MapFromDaoCalled {

            private val model = makeCompanyDao()
            private var result: Company? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDao(model)
            }

            @Test
            @DisplayName("Then should map from dao")
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
        @DisplayName("When we call mapIntoDao")
        inner class MapFromEntityCalled {

            private val entity = makeCompany()
            private var result: CompanyDao? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapIntoDao(entity)
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

    @Nested
    @DisplayName("Given CompanyMapper.DTOMapper instance")
    inner class MapperInstance {

        private lateinit var mapper: CompanyMapper.DTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CompanyMapper.DTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDTO")
        inner class MapFromModelCalled {

            private val sources = makeCompanyDTO()
            var result: Company? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDTO(sources)
            }

            @Test
            @DisplayName("Then should map from DTO")
            fun mapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(sources.id, id)
                    assertEquals(sources.name, name)
                    assertEquals(sources.created_at, createdAt)
                    assertEquals(sources.updated_at, updatedAt)
                    assertEquals(sources.slug, slug)
                    assertEquals(sources.url, url)
                }
            }
        }
    }
}