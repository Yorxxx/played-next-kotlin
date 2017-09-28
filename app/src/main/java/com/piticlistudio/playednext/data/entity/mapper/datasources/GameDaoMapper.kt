package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.CoverEntity
import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.data.entity.TimeToBeatEntity
import com.piticlistudio.playednext.data.entity.dao.CoverCache
import com.piticlistudio.playednext.data.entity.dao.GameCache
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatCache
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import javax.inject.Inject

/**
 * Mapper between [GameCache] and [GameEntity]
 */
class GameDaoMapper @Inject constructor() : LayerDataMapper<GameCache, GameEntity> {

    override fun mapFromModel(type: GameCache): GameEntity {
        with(type) {
            return GameEntity(id, name, url, createdAt, updatedAt, summary, storyline, collection,
                    franchise, hypes, popularity, rating, ratingCount, agregatedRating, aggregatedRatingCount,
                    totalRating, totalRatingCount, firstReleaseAt, mapFromTimeToBeatModel(timeToBeat), mapFromCoverModel(cover))
        }
    }

    override fun mapFromEntity(type: GameEntity): GameCache {
        with(type) {
            return GameCache(id, name, url, createdAt, updatedAt, summary, storyline, collectionId,
                    franchiseId, hypes, popularity, rating, ratingCount, aggregatedRating, aggregatedRatingCount,
                    totalRating, totalRatingCount, firstReleaseAt, mapFromTimeToBeatEntity(timeToBeat), mapFromCoverEntity(cover))
        }
    }

    private fun mapFromCoverModel(type: CoverCache?): CoverEntity? {
        type?.apply {
            return CoverEntity(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatModel(type: TimeToBeatCache?): TimeToBeatEntity? {
        type?.apply {
            return TimeToBeatEntity(hastly, normally, completely)
        }
        return null
    }

    private fun mapFromCoverEntity(type: CoverEntity?): CoverCache? {
        type?.apply {
            return CoverCache(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatEntity(type: TimeToBeatEntity?): TimeToBeatCache? {
        type?.apply {
            return TimeToBeatCache(hastly, normally, completely)
        }
        return null
    }
}