package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CoverDao
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatDao
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.EntityDataMapper
import com.piticlistudio.playednext.domain.model.Cover
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.TimeToBeat
import javax.inject.Inject

/**
 * Mapper between [GameDao] and [Game]
 */
class GameDaoMapper @Inject constructor() :  DaoModelMapper<GameDao, Game>, EntityDataMapper<Game, GameDao> {

    override fun mapFromDao(dao: GameDao): Game {
        with(dao) {
            return Game(id, name, createdAt, updatedAt, summary, storyline, url, rating, ratingCount,
                    agregatedRating, aggregatedRatingCount, totalRating, totalRatingCount,
                    firstReleaseAt, mapFromCoverModel(cover), mapFromTimeToBeatModel(timeToBeat),
                    null, null, null, null, syncedAt, null, null)
        }
    }

    override fun mapIntoDao(entity: Game): GameDao {
        with(entity) {
            return GameDao(id, name, url, createdAt, updatedAt, summary, storyline, null,
                    null, null, null, rating, ratingCount, aggregatedRating,
                    aggregatedRatingCount, totalRating, totalRatingCount, null,
                    mapFromTimeToBeatEntity(timeToBeat), mapFromCoverEntity(cover), syncedAt)
        }
    }

    override fun mapFromEntity(type: GameDao): Game {
        with(type) {
            return Game(id, name, createdAt, updatedAt, summary, storyline, url, rating, ratingCount,
                    agregatedRating, aggregatedRatingCount, totalRating, totalRatingCount,
                    firstReleaseAt, mapFromCoverModel(cover), mapFromTimeToBeatModel(timeToBeat),
                    null, null, null, null, syncedAt, null, null)
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