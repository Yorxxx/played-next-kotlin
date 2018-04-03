package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GenreDaoMapperTest {

    @Nested
    @DisplayName("Given a GenreDaoMapper instance")
    inner class Instance {

        private lateinit var mapper: GenreDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GenreDaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val model = makeGenreDao()
            private var result: Genre? = null

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
                    assertEquals(model.url, url)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntityCalled {

            private val entity = makeGenre()
            private var result: GenreDao? = null

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
                    assertEquals(entity.url, url)
                }
            }
        }
    }


}