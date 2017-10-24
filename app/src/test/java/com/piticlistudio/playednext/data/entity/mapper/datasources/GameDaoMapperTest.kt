package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

internal class GameDaoMapperTest {

    @Nested
    @DisplayName("Given a GameDaoMapper instance")
    inner class GameDaoServiceMapperInstance {

        private lateinit var mapper: GameDaoMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GameDaoMapper()
        }

        @Nested
        @DisplayName("When mapFromEntity is called")
        inner class mapFromEntityCalled {

            val model = GameFactory.makeGameCache()
            var result: Game? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromEntity(model)
            }

            @Test
            @DisplayName("Then should map into Game domain model")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.summary, summary)
                    assertEquals(model.storyline, storyline)
                    //assertEquals(model.collection, collectionId)
                    //assertEquals(model.franchise, franchiseId)
                    assertEquals(model.rating, rating)
                    assertEquals(model.aggregatedRatingCount, aggregatedRatingCount)
                    assertEquals(model.agregatedRating, aggregatedRating)
                    assertEquals(model.createdAt, createdAt)
                    //assertEquals(model.firstReleaseAt, firstReleaseAt)
                    //assertEquals(model.hypes, hypes)
                    //assertEquals(model.popularity, popularity)
                    assertEquals(model.ratingCount, ratingCount)
                    assertEquals(model.totalRating, totalRating)
                }
            }

            @Test
            @DisplayName("Then should map into Cover")
            fun intoCoverEntity() {
                model.cover?.apply {
                    assertNotNull(result!!.cover)
                    assertEquals(url, result!!.cover?.url)
                    assertEquals(width, result!!.cover?.width)
                    assertEquals(height, result!!.cover?.height)
                }
            }

            @Test
            @DisplayName("Then should map into TimeToBeat")
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
        @DisplayName("When mapFromModel is called")
        inner class mapFromModelCalled {

            val entity = GameFactory.makeGame()
            var result: GameDao? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromModel(entity)
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
                    assertNull(collection)
                    assertNull(franchise)
                    assertEquals(entity.rating, rating)
                    assertEquals(entity.aggregatedRatingCount, aggregatedRatingCount)
                    assertEquals(entity.aggregatedRating, agregatedRating)
                    assertEquals(entity.createdAt, createdAt)
                    assertNull(firstReleaseAt)
                    assertNull(hypes)
                    assertNull(popularity)
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