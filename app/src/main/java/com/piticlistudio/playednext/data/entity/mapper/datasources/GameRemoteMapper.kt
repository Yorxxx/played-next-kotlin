package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.CoverEntity
import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.data.entity.TimeToBeatEntity
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.GameRemote
import com.piticlistudio.playednext.data.entity.net.ImageRemote
import com.piticlistudio.playednext.data.entity.net.TimeToBeatRemote
import javax.inject.Inject

class GameRemoteMapper @Inject constructor() : LayerDataMapper<GameRemote, GameEntity> {

    override fun mapFromModel(type: GameRemote): GameEntity {
        with(type) {
            return GameEntity(id, name, url, created_at, updated_at, summary, storyline, collection,
                    franchise, hypes, popularity, rating, rating_count, aggregated_rating, aggregated_rating_count,
                    total_rating, total_rating_count, first_release_date, mapTimeToBeatModel(time_to_beat), mapCoverModel(cover))
        }
    }

    override fun mapFromEntity(type: GameEntity): GameRemote {
        throw Throwable("Forbidden")
    }

    private fun mapTimeToBeatModel(type: TimeToBeatRemote?): TimeToBeatEntity? {
        type?.apply {
            return TimeToBeatEntity(hastly, normally, completely)
        }
        return null
    }

    private fun mapCoverModel(type: ImageRemote?): CoverEntity? {
        type?.apply {
            return CoverEntity(url, width, height)
        }
        return null
    }
}