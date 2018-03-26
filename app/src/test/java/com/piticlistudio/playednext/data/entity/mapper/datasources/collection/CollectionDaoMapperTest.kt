package com.piticlistudio.playednext.data.entity.mapper.datasources.collection

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.CollectionDaoMapper
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDao
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CollectionDaoMapperTest {

    @Nested
    @DisplayName("Given a CollectionDaoMapper instance")
    inner class Instance {

        private lateinit var mapper: CollectionDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = CollectionDaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModelCalled {

            private val model = makeCollectionDao()
            private var result: Collection? = null

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

            @Nested
            @DisplayName("And model is null")
            inner class ModelIsNull {

                private val model: CollectionDao? = null
                private var result: Collection? = null

                @BeforeEach
                internal fun setUp() {
                    result = mapper.mapFromModel(model)
                }

                @Test
                @DisplayName("Then should map model")
                fun isMapped() {
                    assertNull(result)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class MapFromEntityCalled {

            private val entity = makeCollection()
            private var result: CollectionDao? = null

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

            @Nested
            @DisplayName("And model is null")
            inner class ModelIsNull {

                private val model: Collection? = null
                private var result: CollectionDao? = null

                @BeforeEach
                internal fun setUp() {
                    result = mapper.mapFromEntity(model)
                }

                @Test
                @DisplayName("Then should map model")
                fun isMapped() {
                    assertNull(result)
                }
            }
        }
    }
}