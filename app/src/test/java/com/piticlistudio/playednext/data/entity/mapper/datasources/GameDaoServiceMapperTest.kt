package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameCache
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameDaoServiceMapperTest {

    @Nested
    @DisplayName("Given a GameDaoMapper instance")
    inner class GameDaoServiceMapperInstance {

        private lateinit var mapper: GameDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GameDaoMapper()
        }

        @Nested
        @DisplayName("When mapFromModel is called")
        inner class mapFromModelCalled {

            val model = makeGameCache()
            var result: GameDomainModel? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should map into GameDomainModel")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.summary, summary)
                    assertEquals(model.storyline, storyline)
                    assertEquals(model.collection, collectionId)
                    assertEquals(model.franchise, franchiseId)
                    assertEquals(model.rating, rating)
                    assertEquals(model.aggregatedRatingCount, aggregatedRatingCount)
                    assertEquals(model.agregatedRating, aggregatedRating)
                    assertEquals(model.createdAt, createdAt)
                    assertEquals(model.firstReleaseAt, firstReleaseAt)
                    assertEquals(model.hypes, hypes)
                    assertEquals(model.popularity, popularity)
                    assertEquals(model.ratingCount, ratingCount)
                    assertEquals(model.totalRating, totalRating)
                }
            }

            @Test
            @DisplayName("Then should map into CoverDomainModel")
            fun intoCoverEntity() {
                model.cover?.apply {
                    assertNotNull(result!!.cover)
                    assertEquals(url, result!!.cover?.url)
                    assertEquals(width, result!!.cover?.width)
                    assertEquals(height, result!!.cover?.height)
                }
            }

            @Test
            @DisplayName("Then should map into TimeToBeatDomainModel")
            fun intoTimeToBeatEntity() {
                model.timeToBeat?.apply {
                    assertNotNull(result!!.timeToBeat)
                    assertEquals(hastly, result!!.timeToBeat?.hastly)
                    assertEquals(completely, result!!.timeToBeat?.completely)
                    assertEquals(normally, result!!.timeToBeat?.normally)
                }
            }
        }

        @Nested
        @DisplayName("When mapFromEntity is called")
        inner class mapFromEntityCalled {

            val entity = makeGameEntity()
            var result: GameDao? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromEntity(entity)
            }

            @Test
            @DisplayName("Then should map into Dao")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(entity.id, id)
                    assertEquals(entity.name, name)
                    assertEquals(entity.summary, summary)
                    assertEquals(entity.storyline, storyline)
                    assertEquals(entity.collectionId, collection)
                    assertEquals(entity.franchiseId, franchise)
                    assertEquals(entity.rating, rating)
                    assertEquals(entity.aggregatedRatingCount, aggregatedRatingCount)
                    assertEquals(entity.aggregatedRating, agregatedRating)
                    assertEquals(entity.createdAt, createdAt)
                    assertEquals(entity.firstReleaseAt, firstReleaseAt)
                    assertEquals(entity.hypes, hypes)
                    assertEquals(entity.popularity, popularity)
                    assertEquals(entity.ratingCount, ratingCount)
                    assertEquals(entity.totalRating, totalRating)
                }
            }

            @Test
            @DisplayName("Then should map into CoverDao")
            fun intoCoverCache() {
                entity.cover?.apply {
                    assertNotNull(result!!.cover)
                    assertEquals(url, result!!.cover?.url)
                    assertEquals(width, result!!.cover?.width)
                    assertEquals(height, result!!.cover?.height)
                }
            }

            @Test
            @DisplayName("Then should map into TimeToBeatDao")
            fun intoTimeToBeatEntity() {
                entity.timeToBeat?.apply {
                    assertNotNull(result!!.timeToBeat)
                    assertEquals(hastly, result!!.timeToBeat?.hastly)
                    assertEquals(completely, result!!.timeToBeat?.completely)
                    assertEquals(normally, result!!.timeToBeat?.normally)
                }
            }
        }
    }
}