package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.data.entity.igdb.GenreDTO
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDTO
import org.junit.jupiter.api.*

internal class GenreDTOMapperTest {

    @Nested
    @DisplayName("Given GenreDTOMapper instance")
    inner class MapperInstance {

        private lateinit var mapper: GenreDTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GenreDTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val sources = randomListOf { makeGenreDTO() }
            var result: List<Genre>? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(sources)
            }

            @Test
            @DisplayName("Then should map into company list")
            fun mapped() {
                kotlin.test.assertNotNull(result)
                with(result!!) {
                    kotlin.test.assertEquals(sources.size, size)
                    for ((index, value) in this.withIndex()) {
                        kotlin.test.assertEquals(sources[index].id, value.id)
                        kotlin.test.assertEquals(sources[index].name, value.name)
                        kotlin.test.assertEquals(sources[index].url, value.url)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntity {

            private val sources = randomListOf { makeGenre() }
            private var result: List<GenreDTO>? = null

            @Test
            @DisplayName("Then throws error")
            fun errorThrown() {
                try {
                    result = mapper.mapFromEntity(sources)
                    Assertions.fail<String>("Should have thrown")
                } catch (e: Throwable) {
                    kotlin.test.assertNull(result)
                }
            }
        }
    }
}