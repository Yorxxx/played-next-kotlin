package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.nhaarman.mockito_kotlin.verify
import com.piticlistudio.playednext.data.entity.mapper.datasources.CollectionDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.CompanyDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.GenreDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.ImageDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDTOMapper
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
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
import kotlin.test.assertNull


internal class GameDTOMapperTest {

    @Nested
    @DisplayName("Given a GameDTOMapper instance")
    inner class GameDTOMapperInstance {

        @Rule
        @JvmField
        val mOverrideSchedulersRule = RxSchedulersOverrideRule()

        @Mock
        lateinit var companymapper: CompanyDTOMapper
        @Mock lateinit var genremapper: GenreDTOMapper
        @Mock lateinit var collectionmapper: CollectionDTOMapper
        @Mock lateinit var platformmapper: PlatformDTOMapper
        @Mock lateinit var imagesMapper: ImageDTOMapper
        lateinit var mapper: GameDTOMapper

        @BeforeEach
        internal fun setUp() {
            MockitoAnnotations.initMocks(this)
            mapper = GameDTOMapper(companymapper, genremapper, collectionmapper, platformmapper, imagesMapper)
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
                verify(collectionmapper).mapFromModel(model.collection)
                verify(companymapper).mapFromModel(model.publishers)
                verify(companymapper).mapFromModel(model.developers)
                verify(genremapper).mapFromModel(model.genres)
                verify(platformmapper).mapFromModel(model.platforms)
                model.screenshots?.forEach {
                    verify(imagesMapper).mapFromModel(it)
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