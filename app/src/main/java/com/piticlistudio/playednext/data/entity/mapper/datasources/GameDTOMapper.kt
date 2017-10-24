package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.data.entity.net.TimeToBeatDTO
import com.piticlistudio.playednext.domain.model.Cover
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.TimeToBeat
import javax.inject.Inject

class GameDTOMapper @Inject constructor() : LayerDataMapper<GameDTO, Game> {

    override fun mapFromModel(type: GameDTO): Game {
        with(type) {
            return Game(id, name, created_at, updated_at, summary, storyline, url, rating,
                    rating_count, aggregated_rating, aggregated_rating_count, total_rating,
                    total_rating_count, first_release_date, mapCoverModel(cover), mapTimeToBeatModel(time_to_beat))
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