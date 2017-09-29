package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.CoverDomainModel
import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.TimeToBeatDomainModel
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.data.entity.net.TimeToBeatDTO
import javax.inject.Inject

class GameDTOMapper @Inject constructor() : LayerDataMapper<GameDTO, GameDomainModel> {

    override fun mapFromModel(type: GameDTO): GameDomainModel {
        with(type) {
            return GameDomainModel(id, name, url, created_at, updated_at, summary, storyline, collection,
                    franchise, hypes, popularity, rating, rating_count, aggregated_rating, aggregated_rating_count,
                    total_rating, total_rating_count, first_release_date, mapTimeToBeatModel(time_to_beat), mapCoverModel(cover))
        }
    }

    override fun mapFromEntity(type: GameDomainModel): GameDTO {
        throw Throwable("Forbidden")
    }

    private fun mapTimeToBeatModel(type: TimeToBeatDTO?): TimeToBeatDomainModel? {
        type?.apply {
            return TimeToBeatDomainModel(hastly, normally, completely)
        }
        return null
    }

    private fun mapCoverModel(type: ImageDTO?): CoverDomainModel? {
        type?.apply {
            return CoverDomainModel(url, width, height)
        }
        return null
    }
}