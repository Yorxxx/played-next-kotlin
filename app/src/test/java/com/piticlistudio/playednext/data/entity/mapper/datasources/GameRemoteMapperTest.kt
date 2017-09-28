package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.data.entity.net.GameRemote
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameEntity
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull


internal class GameRemoteMapperTest {

    @Nested
    @DisplayName("Given a GameRemoteMapper instance")
    inner class GameRemoteMapperInstance {

        val mapper = GameRemoteMapper()

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModel {

            val model = makeGameRemote()
            var result: GameEntity? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then maps values into model")
            fun valuesAreMapped() {
                assertNotNull(result)
                result?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.url, url)
                    assertEquals(model.created_at, createdAt)
                    assertEquals(model.updated_at, updatedAt)
                    assertEquals(model.summary, summary)
                    assertEquals(model.storyline, storyline)
                    assertEquals(model.collection, collectionId)
                    assertEquals(model.franchise, franchiseId)
                    assertEquals(model.hypes, hypes)
                    assertEquals(model.popularity, popularity)
                    assertEquals(model.rating, rating)
                    assertEquals(model.rating_count, ratingCount)
                    assertEquals(model.aggregated_rating, aggregatedRating)
                    assertEquals(model.aggregated_rating_count, aggregatedRatingCount)
                    assertEquals(model.total_rating, totalRating)
                    assertEquals(model.total_rating_count, totalRatingCount)
                    assertEquals(model.first_release_date, firstReleaseAt)
                    /**
                     *
                    val timeToBeat: TimeToBeatEntity?,
                    val cover: CoverEntity?
                     */
                }
            }

            @Test
            @DisplayName("Then maps into TimeToBeatEntity")
            fun intoTimeToBeatEntity() {
                model.time_to_beat?.apply {
                    assertNotNull(result!!.timeToBeat)
                    assertEquals(hastly, result!!.timeToBeat?.hastly)
                    assertEquals(normally, result!!.timeToBeat?.normally)
                    assertEquals(completely, result!!.timeToBeat?.completely)
                }
            }

            @Test
            @DisplayName("Then maps into CoverEntity")
            fun intoCoverEntity() {
                model.cover?.apply {
                    assertNotNull(result!!.cover)
                    assertEquals(url, result!!.cover?.url)
                    assertEquals(width, result!!.cover?.width)
                    assertEquals(height, result!!.cover?.height)
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntity {

            private val entity = makeGameEntity()
            private var result: GameRemote? = null

            @Test
            @DisplayName("Then throws error")
            fun errorThrown() {
                try {
                    result = mapper.mapFromEntity(entity)
                    fail<String>("SHould have thrown")
                } catch (e: Throwable) {
                    assertNull(result)
                }
            }
        }
    }
}