package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull


internal class GameDTOMapperTest {

    @Nested
    @DisplayName("Given a GameDTOMapper instance")
    inner class GameDTOMapperInstance {

        val mapper = GameDTOMapper()

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class mapFromModel {

            val model = makeGameRemote()
            var result: Game? = null

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
                    assertEquals(model.rating, rating)
                    assertEquals(model.rating_count, ratingCount)
                    assertEquals(model.aggregated_rating, aggregatedRating)
                    assertEquals(model.aggregated_rating_count, aggregatedRatingCount)
                    assertEquals(model.total_rating, totalRating)
                    assertEquals(model.total_rating_count, totalRatingCount)
                }
            }

            @Test
            @DisplayName("Then maps into TimeToBeat")
            fun intoTimeToBeatEntity() {
                model.time_to_beat?.apply {
                    assertNotNull(result!!.timeToBeat)
                    assertEquals(hastly, result!!.timeToBeat?.hastly)
                    assertEquals(normally, result!!.timeToBeat?.normally)
                    assertEquals(completely, result!!.timeToBeat?.completely)
                }
            }

            @Test
            @DisplayName("Then maps into Cover")
            fun intoCoverEntity() {
                model.cover?.apply {
                    assertNotNull(result!!.cover)
                    assertEquals(url, result!!.cover?.url)
                    assertEquals(width, result!!.cover?.width)
                    assertEquals(height, result!!.cover?.height)
                }
            }

            @Test
            @DisplayName("Then maps developers")
            fun intoDevelopers() {
                result?.apply {
                    assertEquals(model.developers?.size, developers?.size)
                    model.developers?.apply {
                        for ((index,value) in model.developers!!.withIndex()) {
                            assertEquals(value.id, developers?.get(index)!!.id)
                            assertEquals(value.name, developers?.get(index)!!.name)
                            assertEquals(value.created_at, developers?.get(index)!!.createdAt)
                            assertEquals(value.updated_at, developers?.get(index)!!.updatedAt)
                            assertEquals(value.slug, developers?.get(index)!!.slug)
                            assertEquals(value.url, developers?.get(index)!!.url)
                        }
                    }
                }
            }

            @Test
            @DisplayName("Then maps publishers")
            fun intoPublishers() {
                result?.apply {
                    assertEquals(model.publishers?.size, publishers?.size)
                    model.publishers?.apply {
                        for ((index,value) in model.publishers!!.withIndex()) {
                            assertEquals(value.id, publishers?.get(index)!!.id)
                            assertEquals(value.name, publishers?.get(index)!!.name)
                            assertEquals(value.created_at, publishers?.get(index)!!.createdAt)
                            assertEquals(value.updated_at, publishers?.get(index)!!.updatedAt)
                            assertEquals(value.slug, publishers?.get(index)!!.slug)
                            assertEquals(value.url, publishers?.get(index)!!.url)
                        }
                    }
                }
            }

            @Test
            @DisplayName("Then maps genres")
            fun intoGenres() {
                result?.apply {
                    assertEquals(model.genres?.size, genres?.size)
                    model.genres?.apply {
                        for ((index,value) in model.genres!!.withIndex()) {
                            assertEquals(value.id, genres?.get(index)!!.id)
                            assertEquals(value.name, genres?.get(index)!!.name)
                            assertEquals(value.created_at, genres?.get(index)!!.createdAt)
                            assertEquals(value.updated_at, genres?.get(index)!!.updatedAt)
                            assertEquals(value.slug, genres?.get(index)!!.slug)
                            assertEquals(value.url, genres?.get(index)!!.url)
                        }
                    }
                }
            }
        }

        @Nested
        @DisplayName("When we call mapFromEntity")
        inner class mapFromEntity {

            private val entity = makeGame()
            private var result: GameDTO? = null

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