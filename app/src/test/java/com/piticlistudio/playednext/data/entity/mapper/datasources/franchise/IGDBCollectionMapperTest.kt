package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.IGDBCollectionMapper
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeIGDBCollection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class IGDBCollectionMapperTest {

    @Nested
    @DisplayName("Given IGDBCollectionMapper instance")
    inner class MapperInstance {

        private lateinit var mapper: IGDBCollectionMapper

        @BeforeEach
        internal fun setUp() {
            mapper = IGDBCollectionMapper()
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            private val source = makeIGDBCollection()
            var result: Collection? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(source)
            }

            @Test
            @DisplayName("Then should map model")
            fun mapped() {
                assertNotNull(result)
                with(result!!) {
                    assertEquals(source.id, id)
                    assertEquals(source.name, name)
                    assertEquals(source.url, url)
                }
            }
        }
    }
}