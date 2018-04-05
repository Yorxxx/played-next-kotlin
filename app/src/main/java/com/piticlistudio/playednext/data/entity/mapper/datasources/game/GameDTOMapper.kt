package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.igdb.GameDTO
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.IGDBCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.IGDBCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.IGDBGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.IGDBImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.IGDBPlatformMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat.IGDBTimeToBeatMapper
import com.piticlistudio.playednext.domain.model.*
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

class GameDTOMapper @Inject constructor(private val igdbCompanyMapper: IGDBCompanyMapper,
                                        private val igdbGenreMapper: IGDBGenreMapper,
                                        private val igdbCollectionMapper: IGDBCollectionMapper,
                                        private val igdbPlatformMapper: IGDBPlatformMapper,
                                        private val imagesMapperIGDB: IGDBImageMapper,
                                        private val igdbTimeToBeatMapper: IGDBTimeToBeatMapper) : LayerDataMapper<GameDTO, Game> {

    override fun mapFromModel(type: GameDTO): Game {
        with(type) {
            val images = mutableListOf<GameImage>()
            screenshots?.forEach {
                imagesMapperIGDB.mapFromDataLayer(it).apply {
                    images.add(GameImage(url, width, height, type.id))
                }
            }
            val devs = mutableListOf<Company>()
            developers?.forEach {
                devs.add(igdbCompanyMapper.mapFromDataLayer(it))
            }
            val pubs = mutableListOf<Company>()
            publishers?.forEach {
                pubs.add(igdbCompanyMapper.mapFromDataLayer(it))
            }
            val gens = mutableListOf<Genre>()
            genres?.forEach {
                gens.add(igdbGenreMapper.mapFromDataLayer(it))
            }
            var col: Collection? = null
            collection?.let {
                col = igdbCollectionMapper.mapFromDataLayer(it)
            }
            val plats = mutableListOf<Platform>()
            platforms?.forEach {
                plats.add(igdbPlatformMapper.mapFromDataLayer(it))
            }
            return Game(
                    id = id,
                    name = name,
                    createdAt = created_at,
                    updatedAt = updated_at,
                    summary = summary,
                    storyline = storyline,
                    url = url,
                    rating = rating,
                    ratingCount = rating_count,
                    aggregatedRating = aggregated_rating,
                    aggregatedRatingCount = aggregated_rating_count,
                    totalRating = total_rating,
                    totalRatingCount = total_rating_count,
                    releasedAt = first_release_date,
                    cover = type.cover?.let { imagesMapperIGDB.mapFromDataLayer(it) },
                    timeToBeat = time_to_beat?.let { igdbTimeToBeatMapper.mapFromDataLayer(time_to_beat) },
                    publishers = pubs,
                    developers = devs,
                    genres = gens,
                    collection = col,
                    syncedAt = System.currentTimeMillis(),
                    platforms = plats,
                    images = images)
        }
    }

    override fun mapFromEntity(type: Game): GameDTO {
        throw Throwable("Forbidden")
    }
}