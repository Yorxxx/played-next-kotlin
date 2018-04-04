package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.IGDBCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.IGDBCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.IGDBGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.IGDBImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.IGDBPlatformMapper
import com.piticlistudio.playednext.data.entity.igdb.GameDTO
import com.piticlistudio.playednext.data.entity.igdb.TimeToBeatDTO
import com.piticlistudio.playednext.domain.model.*
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

class GameDTOMapper @Inject constructor(private val igdbCompanyMapper: IGDBCompanyMapper,
                                        private val igdbGenreMapper: IGDBGenreMapper,
                                        private val igdbCollectionMapper: IGDBCollectionMapper,
                                        private val igdbPlatformMapper: IGDBPlatformMapper,
                                        private val imagesMapperIGDB: IGDBImageMapper) : LayerDataMapper<GameDTO, Game> {

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
            return Game(id, name, created_at, updated_at, summary, storyline, url, rating,
                    rating_count, aggregated_rating, aggregated_rating_count, total_rating,
                    total_rating_count, first_release_date,
                    type.cover?.let { imagesMapperIGDB.mapFromDataLayer(it) },
                    mapTimeToBeatModel(time_to_beat), pubs,
                    devs,
                    gens,
                    col, System.currentTimeMillis(),
                    plats,
                    images)
        }
    }

    override fun mapFromEntity(type: Game): GameDTO {
        throw Throwable("Forbidden")
    }

    private fun mapTimeToBeatModel(type: TimeToBeatDTO?): TimeToBeat? {
        type?.apply {
            return TimeToBeat(hastly, normally, completely)
        }
        return null
    }
}