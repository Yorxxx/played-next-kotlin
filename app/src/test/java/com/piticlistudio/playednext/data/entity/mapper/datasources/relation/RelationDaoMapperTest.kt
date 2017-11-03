package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelation
import com.piticlistudio.playednext.test.factory.GameRelationFactory.Factory.makeGameRelationDao
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RelationDaoMapperTest {

    @Nested
    @DisplayName("Given RelationDaoMapper instance")
    inner class RelationDaoMapperInstance {

        private lateinit var mapper: RelationDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = RelationDaoMapper()
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModelCalled {

            private var result: GameRelation? = null
            private val model = makeGameRelationDao()

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should map into domain model")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.created_at, createdAt)
                    assertEquals(model.updated_at, updatedAt)
                    assertEquals(model.status, currentStatus.ordinal)
                    assertNull(game)
                    assertNull(platform)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntityCalled {

            private var result: GameRelationDao? = null
            private val entity = makeGameRelation()

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromEntity(entity)
            }

            @Test
            @DisplayName("Then should map into data model")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.createdAt, created_at)
                    assertEquals(entity.updatedAt, updated_at)
                    assertEquals(entity.currentStatus.ordinal, status)
                    assertEquals(entity.game?.id, gameId)
                    assertEquals(entity.platform?.id, platformId)
                }
            }
        }
    }
}