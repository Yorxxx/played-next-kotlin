package com.piticlistudio.playednext.data.entity.mapper

import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GameEntityToDomainMapperTest {

    @Nested
    @DisplayName("Given GameEntityToDomainMapper instance")
    inner class GameEntityToDomainMapperInstance {

        private lateinit var mapper: GameEntityToDomainMapper

        @BeforeEach
        internal fun setUp() {
            mapper = GameEntityToDomainMapper()
        }

        @Nested
        @DisplayName("When mapFromModel is called")
        inner class mapFromModelCalled {

            private val model = makeGameEntity()
            private var response: Game? = null

            @BeforeEach
            internal fun setUp() {
                response = mapper.mapFromModel(model)
            }

            @Test
            @DisplayName("Then should map into domain model")
            fun isMapped() {
                assertNotNull(response)
                response?.apply {
                    assertEquals(model.id, id)
                    assertEquals(model.name, name)
                    assertEquals(model.storyline, storyline)
                    assertEquals(model.summary, summary)
                    assertEquals(model.url, url)
                    assertEquals(model.rating, rating)
                    assertEquals(model.ratingCount, ratingCount)
                    assertEquals(model.aggregatedRating, aggregatedRating)
                    assertEquals(model.aggregatedRatingCount, aggregatedRatingCount)
                    assertEquals(model.totalRating, totalRating)
                    assertEquals(model.totalRatingCount, totalRatingCount)
                    assertEquals(model.firstReleaseAt, releasedAt)
                }
            }

            @Test
            @DisplayName("Then should map cover")
            fun coverIsMapped() {
                assertNotNull(response!!.cover)
                response!!.cover?.apply {
                    assertEquals(model.cover?.height, height)
                    assertEquals(model.cover?.width, width)
                    assertEquals(model.cover?.url, url)
                }
            }

            @Test
            @DisplayName("Then should map timeToBeat")
            fun timeToBeatMapped() {
                assertNotNull(response!!.timeToBeat)
                response!!.timeToBeat?.apply {
                    assertEquals(model.timeToBeat?.completely, completely)
                    assertEquals(model.timeToBeat?.hastly, quick)
                    assertEquals(model.timeToBeat?.normally, normally)
                }
            }
        }
    }
}