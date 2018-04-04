package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.room.GameDao
import com.piticlistudio.playednext.data.entity.room.RoomImage
import com.piticlistudio.playednext.data.entity.room.TimeToBeatDao
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Image
import com.piticlistudio.playednext.domain.model.TimeToBeat
import javax.inject.Inject

/**
 * Mapper between [GameDao] and [Game]
 */
class GameDaoMapper @Inject constructor() : LayerDataMapper<Game, GameDao> {

    override fun mapFromEntity(type: GameDao): Game {
        with(type) {
            return Game(id, name, createdAt, updatedAt, summary, storyline, url, rating, ratingCount,
                    agregatedRating, aggregatedRatingCount, totalRating, totalRatingCount,
                    firstReleaseAt, mapFromCoverModel(cover), mapFromTimeToBeatModel(timeToBeat),
                    null, null, null, null, syncedAt, null, null)
        }
    }

    override fun mapFromModel(type: Game): GameDao {
        with(type) {
            return GameDao(id, name, url, createdAt, updatedAt, summary, storyline, null,
                    null, null, null, rating, ratingCount, aggregatedRating,
                    aggregatedRatingCount, totalRating, totalRatingCount, null,
                    mapFromTimeToBeatEntity(timeToBeat), mapFromCoverEntity(cover), syncedAt)
        }
    }

    private fun mapFromCoverModel(type: RoomImage?): Image? {
        type?.apply {
            return Image(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatModel(type: TimeToBeatDao?): TimeToBeat? {
        type?.apply {
            return TimeToBeat(hastly, normally, completely)
        }
        return null
    }

    private fun mapFromCoverEntity(type: Image?): RoomImage? {
        type?.apply {
            return RoomImage(url, width, height)
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