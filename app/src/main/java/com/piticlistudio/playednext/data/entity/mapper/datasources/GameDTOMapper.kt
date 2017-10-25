package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.BaseEnumeratedEntity
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.data.entity.net.TimeToBeatDTO
import com.piticlistudio.playednext.domain.model.*
import javax.inject.Inject

class GameDTOMapper @Inject constructor() : LayerDataMapper<GameDTO, Game> {

    override fun mapFromModel(type: GameDTO): Game {
        with(type) {
            val developers = mutableListOf<Company>()
            val publishers = mutableListOf<Company>()
            val genres = mutableListOf<Genre>()
            type.developers?.forEach {
                developers.add(mapCompanyDTO(it))
            }
            type.publishers?.forEach{
                publishers.add(mapCompanyDTO(it))
            }
            type.genres?.forEach {
                genres.add(mapGenreDTO(it))
            }
            return Game(id, name, created_at, updated_at, summary, storyline, url, rating,
                    rating_count, aggregated_rating, aggregated_rating_count, total_rating,
                    total_rating_count, first_release_date, mapCoverModel(cover),
                    mapTimeToBeatModel(time_to_beat), developers, publishers, genres)
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

    private fun mapCompanyDTO(type: BaseEnumeratedEntity): Company {
        with(type) {
            return Company(id, name, slug, url, created_at, updated_at)
        }
    }

    private fun mapGenreDTO(type: BaseEnumeratedEntity): Genre {
        with(type) {
            return Genre(id, name, slug, url, created_at, updated_at)
        }
    }
}