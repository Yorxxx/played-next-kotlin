package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.IGDBCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.CollectionDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.GenreDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.ImageDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDTOMapper
import com.piticlistudio.playednext.data.entity.igdb.GameDTO
import com.piticlistudio.playednext.data.entity.igdb.ImageDTO
import com.piticlistudio.playednext.data.entity.igdb.TimeToBeatDTO
import com.piticlistudio.playednext.domain.model.*
import javax.inject.Inject

class GameDTOMapper @Inject constructor(private val IGDBCompanyMapper: IGDBCompanyMapper,
                                        private val genreDTOMapper: GenreDTOMapper,
                                        private val collectionDTOMapper: CollectionDTOMapper,
                                        private val platformDTOMapper: PlatformDTOMapper,
                                        private val imagesDTOMapper: ImageDTOMapper) : LayerDataMapper<GameDTO, Game> {

    override fun mapFromModel(type: GameDTO): Game {
        with(type) {
            val images = mutableListOf<GameImage>()
            screenshots?.forEach {
                images.add(imagesDTOMapper.mapFromModel(it))
            }
            val devs = mutableListOf<Company>()
            developers?.forEach {
                devs.add(IGDBCompanyMapper.mapFromDataLayer(it))
            }
            val pubs = mutableListOf<Company>()
            publishers?.forEach {
                pubs.add(IGDBCompanyMapper.mapFromDataLayer(it))
            }
            return Game(id, name, created_at, updated_at, summary, storyline, url, rating,
                    rating_count, aggregated_rating, aggregated_rating_count, total_rating,
                    total_rating_count, first_release_date, mapCoverModel(cover),
                    mapTimeToBeatModel(time_to_beat), pubs,
                    devs,
                    genreDTOMapper.mapFromModel(genres),
                    collectionDTOMapper.mapFromModel(collection), System.currentTimeMillis(),
                    platformDTOMapper.mapFromModel(platforms),
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

    private fun mapCoverModel(type: ImageDTO?): Cover? {
        type?.apply {
            return Cover(url, width, height)
        }
        return null
    }
}