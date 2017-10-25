package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CoverDao
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.Cover
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.TimeToBeat
import javax.inject.Inject

/**
 * Mapper between [GameDao] and [GameDomainModel]
 */
class GameDaoMapper @Inject constructor() : LayerDataMapper<Game, GameDao> {

    override fun mapFromEntity(type: GameDao): Game {
        with(type) {
            return Game(id, name, createdAt, updatedAt, summary, storyline, url, rating, ratingCount,
                    agregatedRating, aggregatedRatingCount, totalRating, totalRatingCount,
                    firstReleaseAt, mapFromCoverModel(cover), mapFromTimeToBeatModel(timeToBeat),
                    null, null, null, null)
        }
    }

    override fun mapFromModel(type: Game): GameDao {
        with(type) {
            return GameDao(id, name, url, createdAt, updatedAt, summary, storyline, null,
                    null, null, null, rating, ratingCount, aggregatedRating,
                    aggregatedRatingCount, totalRating, totalRatingCount, null,
                    mapFromTimeToBeatEntity(timeToBeat), mapFromCoverEntity(cover))
        }
    }

    private fun mapFromCoverModel(type: CoverDao?): Cover? {
        type?.apply {
            return Cover(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatModel(type: TimeToBeatDao?): TimeToBeat? {
        type?.apply {
            return TimeToBeat(hastly, normally, completely)
        }
        return null
    }

    private fun mapFromCoverEntity(type: Cover?): CoverDao? {
        type?.apply {
            return CoverDao(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatEntity(type: TimeToBeat?): TimeToBeatDao? {
        type?.apply {
            return TimeToBeatDao(hastly, normally, completely)
        }
        return null
    }
}