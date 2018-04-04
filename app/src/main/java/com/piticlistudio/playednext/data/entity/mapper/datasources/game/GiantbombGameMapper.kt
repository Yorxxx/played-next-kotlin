package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGame
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.GiantbombCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.GiantbombCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.GiantbombGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.GiantbombImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.GiantbombPlatformMapper
import com.piticlistudio.playednext.domain.model.*
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

/**
 * Mapper for converting [GiantbombGame] into [Game]
 * Created by e-jegi on 3/26/2018.
 */
class GiantbombGameMapper @Inject constructor(private val companyMapper: GiantbombCompanyMapper,
                                              private val genreMapper: GiantbombGenreMapper,
                                              private val collectionMapper: GiantbombCollectionMapper,
                                              private val platformMapper: GiantbombPlatformMapper,
                                              private val imagesMapper: GiantbombImageMapper) : DataLayerMapper<GiantbombGame, Game> {

    override fun mapFromDataLayer(model: GiantbombGame): Game {
        with(model) {
            val devs = mutableListOf<Company>().apply {
                developers?.forEach {
                    add(companyMapper.mapFromDataLayer(it))
                }
            }
            val pubs = mutableListOf<Company>().apply {
                publishers?.forEach {
                    add(companyMapper.mapFromDataLayer(it))
                }
            }
            val genres = mutableListOf<Genre>().apply {
                genres?.forEach {
                    add(genreMapper.mapFromDataLayer(it))
                }
            }
            val plats = mutableListOf<Platform>().apply {
                platforms?.forEach {
                    add(platformMapper.mapFromDataLayer(it))
                }
            }
            val screens = mutableListOf<GameImage>().apply {
                images?.forEach {
                    val result = imagesMapper.mapFromDataLayer(it)
                    result?.let { add(GameImage(it.url, it.width, it.height, model.id)) }
                }
            }
            val collection: Collection? = model.franchises?.first()?.let { collectionMapper.mapFromDataLayer(it) }
            val cover: Image? = model.image?.let { imagesMapper.mapFromDataLayer(it) }

            return Game(id = model.id,
                    name = model.name,
                    createdAt = model.date_added.time,
                    updatedAt = model.date_last_updated.time,
                    developers = devs,
                    publishers = pubs,
                    genres = genres,
                    aggregatedRating = null,
                    collection = collection,
                    cover = cover,
                    images = screens,
                    aggregatedRatingCount = null,
                    platforms = plats,
                    rating = null,
                    ratingCount = null,
                    releasedAt = model.original_release_date?.time,
                    storyline = model.description,
                    summary = model.deck,
                    syncedAt = model.date_last_updated.time,
                    timeToBeat = null,
                    totalRating = null,
                    totalRatingCount = null,
                    url = model.site_detail_url)
        }
    }
}