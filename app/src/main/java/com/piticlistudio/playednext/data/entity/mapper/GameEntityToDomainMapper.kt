package com.piticlistudio.playednext.data.entity.mapper

import com.piticlistudio.playednext.data.entity.CoverDomainModel
import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.TimeToBeatDomainModel
import com.piticlistudio.playednext.domain.model.game.Cover
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.model.game.TimeToBeat
import javax.inject.Inject

/**
 * Maps a [GameDomainModel] into a [Game] entity
 */
open class GameEntityToDomainMapper @Inject constructor() : LayerDataMapper<GameDomainModel, Game> {

    override fun mapFromModel(type: GameDomainModel): Game {
        with(type) {
            return Game(id, name, summary, storyline, url, rating, ratingCount, aggregatedRating,
                    aggregatedRatingCount, totalRating, totalRatingCount, firstReleaseAt,
                    mapFromCoverModel(cover), mapFromTimeToBeatModel(timeToBeat))
        }
    }

    override fun mapFromEntity(type: Game): GameDomainModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun mapFromCoverModel(type: CoverDomainModel?): Cover? {
        type?.apply {
            return Cover(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatModel(type: TimeToBeatDomainModel?): TimeToBeat? {
        type?.apply {
            return TimeToBeat(hastly, normally, completely)
        }
        return null
    }
}