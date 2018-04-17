package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeIGDBGenre
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class IGDBGenreMapperTest {

    @Nested
    @DisplayName("Given IGDBGenreMapper instance")
    inner class MapperInstance {

        private lateinit var mapper: IGDBGenreMapper

        @BeforeEach
        internal fun setUp() {
            mapper = IGDBGenreMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromModelCalled {

            private val source = makeIGDBGenre()
            var result: Genre? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should map into genre")
            fun mapped() {
                assertNotNull(result)
                assertEquals(source.id, result?.id)
                assertEquals(source.name, result?.name)
                assertEquals(source.url, result?.url)
            }
        }
    }
}