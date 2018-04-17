package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat.RoomTimeToBeatMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameProxy
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeRoomGame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

internal class RoomGameMapperTest {

    @Nested
    @DisplayName("Given a RoomGameMapper instance")
    inner class RoomGameServiceMapperInstance {

        private lateinit var mapper: RoomGameMapper
        private val timeToBeatMapper: RoomTimeToBeatMapper = mock()

        @BeforeEach
        internal fun setUp() {
            reset(timeToBeatMapper)
            mapper = RoomGameMapper(timeToBeatMapper)
        }

        @Nested
        @DisplayName("When mapFromEntity is called")
        inner class MapFromEntityCalled {

            var model = GameFactory.makeRoomGame()
            var result: Game? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapFromDataLayer(RoomGameProxy(model))
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
                    assertEquals(model.syncedAt, syncedAt)
                    assertEquals(model.rating, rating)
                    assertEquals(model.aggregatedRatingCount, aggregatedRatingCount)
                    assertEquals(model.agregatedRating, aggregatedRating)
                    assertEquals(model.createdAt, createdAt)
                    assertEquals(model.firstReleaseAt, releasedAt)
                    assertEquals(model.ratingCount, ratingCount)
                    assertEquals(model.totalRatingCount, totalRatingCount)
                    assertEquals(model.totalRating, totalRating)
                    assertNotNull(developers)
                    assertNotNull(publishers)
                    assertNotNull(genres)
                    assertNotNull(platforms)
                    assertNotNull(images)
                    assertNull(collection)
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
            @DisplayName("Then should set null cover when source is missing")
            fun nullCover() {
                model = makeRoomGame(cover = null)

                // Act
                result = mapper.mapFromDataLayer(RoomGameProxy(model))

                assertNotNull(result)
                result?.apply {
                    assertNull(cover)
                }
            }

            @Test
            @DisplayName("Then should map into TimeToBeat")
            fun intoTimeToBeatEntity() {
                model.timeToBeat?.let {
                    verify(timeToBeatMapper).mapFromDataLayer(it)
                }
            }

            @Test
            @DisplayName("Then should set null timetobeat when source is missing")
            fun nullTimetoBeat() {
                model = makeRoomGame(timeToBeat = null)

                // Act
                result = mapper.mapFromDataLayer(RoomGameProxy(model))

                assertNotNull(result)
                result?.apply {
                    assertNull(timeToBeat)
                }
            }
        }

        @Nested
        @DisplayName("When mapIntoDataLayerModel is called")
        inner class MapIntoDataLayerModelCalled {

            var entity = GameFactory.makeGame()
            var result: RoomGameProxy? = null

            @BeforeEach
            internal fun setUp() {
                result = mapper.mapIntoDataLayerModel(entity)
            }

            @Test
            @DisplayName("Then should map into Dao")
            fun shouldMap() {
                assertNotNull(result)
                result?.apply {
                    assertNotNull(game)
                    game.apply {
                        assertEquals(entity.id, id)
                        assertEquals(entity.name, name)
                        assertEquals(entity.summary, summary)
                        assertEquals(entity.storyline, storyline)
                        assertEquals(entity.collection?.id, collection)
                        assertNull(franchise)
                        assertEquals(entity.rating, rating)
                        assertEquals(entity.aggregatedRatingCount, aggregatedRatingCount)
                        assertEquals(entity.aggregatedRating, agregatedRating)
                        assertEquals(entity.createdAt, createdAt)
                        assertEquals(entity.releasedAt, firstReleaseAt)
                        assertNull(hypes)
                        assertNull(popularity)
                        assertEquals(entity.ratingCount, ratingCount)
                        assertEquals(entity.totalRating, totalRating)
                        assertEquals(entity.syncedAt, syncedAt)
                        entity.cover?.let {
                            assertNotNull(this.cover)
                            assertEquals(it.height, this.cover!!.height)
                            assertEquals(it.width, this.cover!!.width)
                            assertEquals(it.url, this.cover!!.url)
                        }
                    }
                }
            }


            @Test
            @DisplayName("Then should map into RoomTimeToBeat")
            fun intoTimeToBeatEntity() {
                entity.timeToBeat?.apply {
                    verify(timeToBeatMapper).mapIntoDataLayerModel(this)
                }
            }

            @Test
            @DisplayName("Then should set null timetobeat when source is missing")
            fun nullTimetoBeat() {
                entity = makeGame(timetoBeat = null)

                // Act
                result = mapper.mapIntoDataLayerModel(entity)

                assertNotNull(result)
                result?.apply {
                    assertNotNull(game)
                    game.apply {
                        assertNull(timeToBeat)
                    }
                }
            }

            @Test
            @DisplayName("Then should set null cover when source is missing")
            fun nullCover() {
                entity = makeGame(cover = null)

                // Act
                result = mapper.mapIntoDataLayerModel(entity)

                assertNotNull(result)
                result?.apply {
                    assertNotNull(game)
                    game.apply {
                        assertNull(cover)
                    }
                }
            }

            @Test
            @DisplayName("Then should set null collection when source is missing")
            fun nullCollection() {
                entity = makeGame(collection = null)

                // Act
                result = mapper.mapIntoDataLayerModel(entity)

                assertNotNull(result)
                result?.apply {
                    assertNotNull(game)
                    game.apply {
                        assertNull(collection)
                    }
                }
            }
        }
    }
}