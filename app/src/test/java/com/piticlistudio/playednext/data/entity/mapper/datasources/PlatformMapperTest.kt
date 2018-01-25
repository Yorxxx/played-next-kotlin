package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.PlatformFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlatformMapperTest {

    @Nested
    @DisplayName("Given a PlatformMapper.DaoMapper instance")
    inner class Instance {

        private lateinit var mapper: PlatformMapper.DaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = PlatformMapper.DaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDao")
        inner class MapFromModelCalled {

            private val model = PlatformFactory.makePlatformDao()
            private var result: Platform? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDao(model)
            }

            @Test
            @DisplayName("Then should map from dao")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.created_at, createdAt)
                    assertEquals(model.updated_at, updatedAt)
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.slug, slug)
                    assertEquals(model.url, url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapIntoDao")
        inner class MapFromEntityCalled {

            private val model = PlatformFactory.makePlatform()
            private var result: PlatformDao? = null

            @BeforeEach
            internal fun setup() {
                result = mapper.mapIntoDao(model)
            }

            @Test
            @DisplayName("Then should map into dao")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.createdAt, created_at)
                    assertEquals(model.updatedAt, updated_at)
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.slug, slug)
                    assertEquals(model.url, url)
                }
            }
        }
    }

    @Nested
    @DisplayName("Given a PlatformMapper.DTOMapper instance")
    inner class DTOInstance {

        private lateinit var mapper: PlatformMapper.DTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = PlatformMapper.DTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDTO")
        inner class MapFromModelCalled {

            private val model = PlatformFactory.makePlatformDTO()
            private var result: Platform? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDTO(model)
            }

            @Test
            @DisplayName("Then should map model")
            fun isMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.created_at, createdAt)
                    assertEquals(model.updated_at, updatedAt)
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.slug, slug)
                    assertEquals(model.url, url)
                }
            }
        }
    }
}