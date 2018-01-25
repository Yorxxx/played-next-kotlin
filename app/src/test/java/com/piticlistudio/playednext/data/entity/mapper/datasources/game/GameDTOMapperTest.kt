package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.net.*
import com.piticlistudio.playednext.domain.model.*
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGameRemote
import com.piticlistudio.playednext.util.RxSchedulersOverrideRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


internal class GameDTOMapperTest {

    @Nested
    @DisplayName("Given a GameDTOMapper instance")
    inner class GameDTOMapperInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        lateinit var companymapper: DTOModelMapper<CompanyDTO, Company>
        @Mock lateinit var genremapper: DTOModelMapper<GenreDTO, Genre>
        @Mock lateinit var collectionmapper: DTOModelMapper<CollectionDTO, Collection>
        @Mock lateinit var platformmapper: DTOModelMapper<PlatformDTO, Platform>
        @Mock lateinit var imagesMapper: DTOModelMapper<ImageDTO, GameImage>
        lateinit var mapper: GameDTOMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            mapper = GameDTOMapper(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper)
        }

        @Nested
        @DisplayName("When we call mapFromDTO")
        inner class MapFromModel {

            val model = makeGameRemote()
            var result: Game? = null

            @BeforeEach
            fun setup() {
                result = mapper.mapFromDTO(model)
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
                verify(collectionmapper).mapFromDTO(model.collection!!)
                model.developers?.forEach {
                    verify(companymapper).mapFromDTO(it)
                }
                model.publishers?.forEach {
                    verify(companymapper).mapFromDTO(it)
                }
                model.genres?.forEach {
                    verify(genremapper).mapFromDTO(it)
                }
                model.platforms?.forEach {
                    verify(platformmapper).mapFromDTO(it)
                }
                model.screenshots?.forEach {
                    verify(imagesMapper).mapFromDTO(it)
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
        }
    }
}