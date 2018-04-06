package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.igdb.IGDBGame
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.IGDBCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.IGDBCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.IGDBGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.IGDBImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.IGDBPlatformMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat.IGDBTimeToBeatMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeIGDBGame
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeImage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue


internal class IGDBGameMapperTest {

    @Nested
    @DisplayName("Given a IGDBGameMapper instance")
    inner class IGDBGameMapperInstance {

        val companymapper: IGDBCompanyMapper = mock()
        val genremapper: IGDBGenreMapper = mock()
        val collectionmapper: IGDBCollectionMapper = mock()
        val platformmapper: IGDBPlatformMapper = mock()
        val imagesMapper: IGDBImageMapper = mock()
        val timeToBeatMapper: IGDBTimeToBeatMapper = mock()
        lateinit var mapper: IGDBGameMapper

        @BeforeEach
        internal fun setUp() {
            reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
            mapper = IGDBGameMapper(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
            whenever(imagesMapper.mapFromDataLayer(any())).thenReturn(makeImage())
        }

        @Nested
        @DisplayName("When we call mapFromDataLayer")
        inner class MapFromDataLayerCalled {

            val model = makeIGDBGame()
            var result: Game? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromDataLayer(model)
            }

            @Test
            @DisplayName("Then maps values into model")
            fun valuesAreMapped() {
                assertNotNull(result)
                val currentTime = System.currentTimeMillis()
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
                    assertTrue(syncedAt <= currentTime)
                    assertTrue { syncedAt > 0 }
                }
            }

            @Test
            @DisplayName("Then requests mapping for inner classes")
            fun childMaps() {
                model.collection?.let {
                    verify(collectionmapper).mapFromDataLayer(it)
                }
                model.publishers?.forEach {
                    verify(companymapper).mapFromDataLayer(it)
                }
                model.developers?.forEach {
                    verify(companymapper).mapFromDataLayer(it)
                }
                model.genres?.forEach {
                    verify(genremapper).mapFromDataLayer(it)
                }
                model.platforms?.forEach {
                    verify(platformmapper).mapFromDataLayer(it)
                }
                model.screenshots?.forEach {
                    verify(imagesMapper).mapFromDataLayer(it)
                }
                model.cover?.let {
                    verify(imagesMapper).mapFromDataLayer(it)
                }
                model.time_to_beat?.let {
                    verify(timeToBeatMapper).mapFromDataLayer(it)
                }
            }

            @Nested
            @DisplayName("And source does not have screenshots")
            inner class WithoutScreenshots {

                val model = makeIGDBGame(images = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set an empty list as entity screenshots")
                fun emptyList() {
                    assertNotNull(result)
                    result?.let {
                        assertNotNull(it.images)
                        assertTrue(it.images?.isEmpty()!!)
                    }
                }
            }

            @Nested
            @DisplayName("And source does not have developers")
            inner class WithoutDevelopers {

                val model = makeIGDBGame(developers = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set an empty list as entity developers")
                fun emptyList() {
                    assertNotNull(result)
                    result?.let {
                        assertNotNull(it.developers)
                        assertTrue(it.developers?.isEmpty()!!)
                    }
                }
            }

            @Nested
            @DisplayName("And source does not have publishers")
            inner class WithoutPublishers {

                val model = makeIGDBGame(publishers = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set an empty list as entity publishers")
                fun emptyList() {
                    assertNotNull(result)
                    result?.let {
                        assertNotNull(it.publishers)
                        assertTrue(it.publishers?.isEmpty()!!)
                    }
                }
            }

            @Nested
            @DisplayName("And source does not have genres")
            inner class WithoutGenres {

                val model = makeIGDBGame(genres = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set an empty list as entity genres")
                fun emptyList() {
                    assertNotNull(result)
                    result?.let {
                        assertNotNull(it.genres)
                        assertTrue(it.genres?.isEmpty()!!)
                    }
                }
            }

            @Nested
            @DisplayName("And source does not have collection")
            inner class WithoutCollection {

                val model = makeIGDBGame(collection = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set a null collection")
                fun emptyList() {
                    assertNotNull(result)
                    assertNull(result?.collection)
                }
            }

            @Nested
            @DisplayName("And source does not have platforms")
            inner class WithoutPlatforms {

                val model = makeIGDBGame(platforms = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set an empty list as entity platforms")
                fun emptyList() {
                    assertNotNull(result)
                    result?.let {
                        assertNotNull(it.platforms)
                        assertTrue(it.platforms?.isEmpty()!!)
                    }
                }
            }

            @Nested
            @DisplayName("And source does not have cover")
            inner class WithoutCover {

                val model = makeIGDBGame(cover = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set a null collection")
                fun emptyList() {
                    assertNotNull(result)
                    assertNull(result?.cover)
                }
            }

            @Nested
            @DisplayName("And source does not have timeToBeat")
            inner class WithoutTimeToBeat {

                val model = makeIGDBGame(timeToBeat = null)

                @BeforeEach
                internal fun setUp() {
                    reset(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
                    result = mapper.mapFromDataLayer(model)
                }

                @Test
                @DisplayName("Then should set a null time to beat")
                fun emptyList() {
                    assertNotNull(result)
                    assertNull(result?.timeToBeat)
                }
            }
        }
    }
}