package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDTOList
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreList
import org.junit.jupiter.api.*

internal class GenreDTOMapperTest {

    @Nested
    @DisplayName("Given GenreDTOMapper instance")
    inner class mapperInstance {

        private lateinit var mapper: GenreDTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GenreDTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModelCalled {

            val sources = makeGenreDTOList()
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
                        kotlin.test.assertEquals(sources.get(index).id, value.id)
                        kotlin.test.assertEquals(sources.get(index).name, value.name)
                        kotlin.test.assertEquals(sources.get(index).created_at, value.createdAt)
                        kotlin.test.assertEquals(sources.get(index).updated_at, value.updatedAt)
                        kotlin.test.assertEquals(sources.get(index).slug, value.slug)
                        kotlin.test.assertEquals(sources.get(index).url, value.url)
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntity {

            private val sources = makeGenreList()
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