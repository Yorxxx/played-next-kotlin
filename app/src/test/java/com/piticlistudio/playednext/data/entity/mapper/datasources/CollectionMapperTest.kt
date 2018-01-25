package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CollectionMapperTest {

    @Nested
    @DisplayName("Given a CollectionMapper.DaoMapper instance")
    inner class Instance {

        private lateinit var mapper: CollectionMapper.DaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CollectionMapper.DaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDao")
        inner class MapFromModelCalled {

            private val model = CollectionFactory.makeCollectionDao()
            private var result: Collection? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDao(model)
            }

            @Test
            @DisplayName("Then should map into entity")
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
        inner class MapIntoDaoCalled {

            private val entity = CollectionFactory.makeCollection()
            private var result: CollectionDao? = null

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
    @DisplayName("Given CollectionMapper.DTOMapper instance")
    inner class MapperInstance {

        private lateinit var mapper: CollectionMapper.DTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CollectionMapper.DTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDTO")
        inner class MapFromDTOCalled {

            private val source = CollectionFactory.makeCollectionDTO()
            var result: Collection? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDTO(source)
            }

            @Test
            @DisplayName("Then should map")
            fun mapped() {
                assertNotNull(result)
                with(result!!) {
                    assertEquals(source.id, id)
                    assertEquals(source.name, name)
                    assertEquals(source.created_at, createdAt)
                    assertEquals(source.updated_at, updatedAt)
                    assertEquals(source.slug, slug)
                    assertEquals(source.url, url)
                }
            }
        }
    }
}