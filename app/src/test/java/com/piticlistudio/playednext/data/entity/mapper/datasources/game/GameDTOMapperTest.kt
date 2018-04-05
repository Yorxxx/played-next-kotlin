package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.piticlistudio.playednext.data.entity.igdb.GameDTO
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.IGDBCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.IGDBCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.IGDBGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.IGDBImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.IGDBPlatformMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat.IGDBTimeToBeatMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeImage
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertNull


internal class GameDTOMapperTest {

    @Nested
    @DisplayName("Given a GameDTOMapper instance")
    inner class GameDTOMapperInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        val companymapper: IGDBCompanyMapper = mock()
        val genremapper: IGDBGenreMapper = mock()
        val collectionmapper: IGDBCollectionMapper = mock()
        val platformmapper: IGDBPlatformMapper = mock()
        val imagesMapper: IGDBImageMapper = mock()
        val timeToBeatMapper: IGDBTimeToBeatMapper = mock()
        lateinit var mapper: GameDTOMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            mapper = GameDTOMapper(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper, timeToBeatMapper)
            whenever(imagesMapper.mapFromDataLayer(any())).thenReturn(makeImage())
        }

        @Nested
        @DisplayName("When we call mapFromModel")
        inner class MapFromModel {

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