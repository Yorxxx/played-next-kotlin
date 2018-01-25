package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.GenreFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GenreMapperTest {

    @Nested
    @DisplayName("Given a GenreMapper.DaoMapper instance")
    inner class Instance {

        private lateinit var mapper: GenreMapper.DaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GenreMapper.DaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDao")
        inner class MapFromModelCalled {

            private val model = GenreFactory.makeGenreDao()
            private var result: Genre? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDao(model)
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
        @DisplayName("When we call mapIntoDao")
        inner class MapFromEntityCalled {

            private val entity = GenreFactory.makeGenre()
            private var result: GenreDao? = null

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
    @DisplayName("Given GenreMapper.DTOMapper instance")
    inner class MapperInstance {

        private lateinit var mapper: GenreMapper.DTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GenreMapper.DTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDTO")
        inner class MapFromModelCalled {

            private val sources = GenreFactory.makeGenreDTO()
            var result: Genre? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDTO(sources)
            }

            @Test
            @DisplayName("Then should map into company")
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