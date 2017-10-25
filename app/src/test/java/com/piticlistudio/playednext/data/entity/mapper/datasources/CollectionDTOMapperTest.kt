package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.net.CollectionDTO
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDTO
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

internal class CollectionDTOMapperTest {

    @Nested
    @DisplayName("Given CollectionDTOMapper instance")
    inner class mapperInstance {

        private lateinit var mapper: CollectionDTOMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CollectionDTOMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModelCalled {

            val source = makeCollectionDTO()
            var result: Collection? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(source)
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

            @Nested
            @DisplayName("And model is null")
            inner class modelIsNull {

                private val model: CollectionDTO? = null
                private var result: Collection? = null

                @BeforeEach
                internal fun setUp() {
                    result = mapper.mapFromModel(model)
                }

                @Test
                @DisplayName("Then should map model")
                fun isMapped() {
                    Assertions.assertNull(result)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntity {

            private val source = makeCollection()
            private var result: CollectionDTO? = null

            @Test
            @DisplayName("Then throws error")
            fun errorThrown() {
                try {
                    result = mapper.mapFromEntity(source)
                    Assertions.fail<String>("Should have thrown")
                } catch (e: Throwable) {
                    kotlin.test.assertNull(result)
                }
            }
        }
    }
}