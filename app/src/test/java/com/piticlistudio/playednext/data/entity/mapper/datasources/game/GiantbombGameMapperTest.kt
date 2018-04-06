package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.nhaarman.mockito_kotlin.*
import com.piticlistudio.playednext.data.entity.giantbomb.*
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.GiantbombCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.GiantbombCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.GiantbombGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.GiantbombImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.GiantbombPlatformMapper
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGiantbombGame
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test cases for [GiantbombGameMapper]
 * Created by e-jegi on 3/26/2018.
 */
internal class GiantbombGameMapperTest {

    private lateinit var game: GiantbombGame
    private val companyMapper: GiantbombCompanyMapper = mock()
    private val genreMapper: GiantbombGenreMapper = mock()
    private val collectionMapper: GiantbombCollectionMapper = mock()
    private val platformMapper: GiantbombPlatformMapper = mock()
    private val imagesMapper: GiantbombImageMapper = mock()
    private lateinit var mapper: GiantbombGameMapper

    @BeforeEach
    internal fun setUp() {
        game = makeGiantbombGame()
        reset(companyMapper, genreMapper, collectionMapper, platformMapper, imagesMapper)
        mapper = GiantbombGameMapper(companyMapper, genreMapper, collectionMapper, platformMapper, imagesMapper)
    }

    @Nested
    @DisplayName("When we call mapFromDataLayer")
    inner class MapFromDataLayer {

        @Test
        fun `then should request GiantbombCompanyMapper to map the developers`() {

            doAnswer {
                val arg = it.arguments[0] as GiantbombCompany
                makeCompany(arg.name, arg.id)
            }
                    .whenever(companyMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            game.developers?.forEachIndexed { index, giantbombCompany ->
                verify(companyMapper).mapFromDataLayer(giantbombCompany)
                assertEquals(giantbombCompany.id, result.developers.get(index).id)
                assertEquals(giantbombCompany.name, result.developers.get(index).name)
            }
            assertEquals(game.developers?.size, result.developers.size)
        }

        @Test
        fun `then should set empty devs when no developers available`() {

            game = makeGiantbombGame(developers = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            with(result.developers) {
                assertNotNull(this)
                assertTrue(isEmpty())
            }
        }

        @Test
        fun `then should request GiantbombCompanyMapper to map the publishers`() {

            doAnswer {
                val arg = it.arguments[0] as GiantbombCompany
                makeCompany(arg.name, arg.id)
            }
                    .whenever(companyMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            game.publishers?.forEachIndexed { index, giantbombCompany ->
                verify(companyMapper).mapFromDataLayer(giantbombCompany)
                assertEquals(giantbombCompany.id, result.publishers.get(index).id)
                assertEquals(giantbombCompany.name, result.publishers.get(index).name)
            }
            assertEquals(game.publishers?.size, result.publishers.size)
        }

        @Test
        fun `then should set empty publishers when no publishers available`() {

            game = makeGiantbombGame(publishers = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            with(result.publishers) {
                assertNotNull(this)
                assertTrue(isEmpty())
            }
        }

        @Test
        fun `then should request GiantBombGenreMapper to map the genres`() {

            doAnswer {
                val arg = it.arguments[0] as GiantbombGenre
                makeGenre(arg.name, arg.id)
            }
                    .whenever(genreMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            game.genres?.forEachIndexed { index, genre ->
                verify(genreMapper).mapFromDataLayer(genre)
                assertEquals(genre.id, result.genres.get(index).id)
                assertEquals(genre.name, result.genres.get(index).name)
            }
            assertEquals(game.genres?.size, result.genres.size)
        }

        @Test
        fun `then should set empty genres when no genres available`() {

            game = makeGiantbombGame(genres = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            with(result.genres) {
                assertNotNull(this)
                assertTrue(isEmpty())
            }
            verifyZeroInteractions(genreMapper)
        }

        @Test
        fun `then should request GiantbombCollectionMapper to map the collections`() {

            var answer: Collection? = null
            doAnswer {
                val arg = it.arguments[0] as GiantbombFranchise
                answer = makeCollection(arg.id, arg.name)
                answer
            }.whenever(collectionMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            verify(collectionMapper).mapFromDataLayer(game.franchises?.first()!!)
            assertEquals(answer?.id, result.collection?.id)
        }

        @Test
        fun `then should set null collection when no franchises available`() {

            game = makeGiantbombGame(franchises = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.collection)
            verifyZeroInteractions(collectionMapper)
        }

        @Test
        fun `then should set null collection when empty franchises`() {

            game = makeGiantbombGame(franchises = listOf())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.collection)
            verifyZeroInteractions(collectionMapper)
        }


        @Test
        fun `then should request to map the platforms`() {

            doAnswer {
                val arg = it.arguments[0] as GiantbombPlatform
                makePlatform(arg.id, arg.name)
            }
                    .whenever(platformMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            game.platforms?.forEachIndexed { index, platform ->
                verify(platformMapper).mapFromDataLayer(platform)
                assertEquals(platform.id, result.platforms.get(index).id)
                assertEquals(platform.name, result.platforms.get(index).name)
            }
            assertEquals(game.platforms?.size, result.platforms.size)
        }

        @Test
        fun `then should set empty platforms when no platforms available`() {

            game = makeGiantbombGame(platforms = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            with(result.platforms) {
                assertNotNull(this)
                assertTrue(isEmpty())
            }
            verifyZeroInteractions(platformMapper)
        }

        @Test
        fun `then should request to map the screenshots`() {

            doAnswer {
                makeGameImage()
            }.whenever(imagesMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.images?.size, result.images.size)
            game.images?.forEachIndexed { index, image ->
                verify(imagesMapper).mapFromDataLayer(image)
            }
        }

        @Test
        fun `then should set empty images when no images available`() {

            game = makeGiantbombGame(images = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            with(result.images) {
                assertNotNull(this)
                assertTrue(isEmpty())
            }
        }

        @Test
        fun `then should request to map the cover`() {

            var answer: GameImage? = null
            doAnswer {
                answer = makeGameImage()
                answer
            }.whenever(imagesMapper).mapFromDataLayer(any())

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNotNull(result.cover)
            game.image?.let {
                verify(imagesMapper).mapFromDataLayer(it)

            }
            assertEquals(result.cover?.url, answer?.url)
        }

        @Test
        fun `then should set null cover when no cover available`() {

            game = makeGiantbombGame(cover = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.cover)
        }

        @Test
        fun `then should map the id`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.id, result.id)
        }

        @Test
        fun `then should map the name`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.name, result.name)
        }

        @Test
        fun `then should map the creation timestamp value`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.date_added.time, result.createdAt)
        }

        @Test
        fun `then should map the updated timestamp value`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.date_last_updated.time, result.updatedAt)
        }

        @Test
        fun `then should map the storyline value`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.description, result.storyline)
        }

        @Test
        fun `then should map the summary value`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.deck, result.summary)
        }

        @Test
        fun `then should map the url value`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.site_detail_url, result.url)
        }

        @Test
        fun `then should map the original release date value`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertEquals(game.original_release_date?.time, result.releasedAt)
        }

        @Test
        fun `then should null releasedAt value if no original_release_date value`() {

            game = makeGiantbombGame(original_release_date = null)

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.releasedAt)
        }

        @Test
        fun `then should set the aggregatedRating to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.aggregatedRating)
        }

        @Test
        fun `then should set the aggregatedRatingCount to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.aggregatedRatingCount)
        }

        @Test
        fun `then should set the rating to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.rating)
        }

        @Test
        fun `then should set the ratingCount to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.ratingCount)
        }

        @Test
        fun `then should set the totalRating to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.totalRating)
        }

        @Test
        fun `then should set the totalRatingCount to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.totalRatingCount)
        }

        @Test
        fun `then should set the timeToBeat to null`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNull(result.timeToBeat)
        }

        @Test
        fun `then should set the syncedAt value to 0`() {

            val result = mapper.mapFromDataLayer(game)

            assertNotNull(result)
            assertNotNull(result.syncedAt)
            assertEquals(0L, result.syncedAt)
        }
    }
}