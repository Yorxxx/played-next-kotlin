package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.CoverDomainModel
import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.TimeToBeatDomainModel
import com.piticlistudio.playednext.data.entity.dao.CoverDao
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import javax.inject.Inject

/**
 * Mapper between [GameDao] and [GameDomainModel]
 */
class GameDaoMapper @Inject constructor() : LayerDataMapper<GameDao, GameDomainModel> {

    override fun mapFromModel(type: GameDao): GameDomainModel {
        with(type) {
            return GameDomainModel(id, name, url, createdAt, updatedAt, summary, storyline, collection,
                    franchise, hypes, popularity, rating, ratingCount, agregatedRating, aggregatedRatingCount,
                    totalRating, totalRatingCount, firstReleaseAt, mapFromTimeToBeatModel(timeToBeat), mapFromCoverModel(cover))
        }
    }

    override fun mapFromEntity(type: GameDomainModel): GameDao {
        with(type) {
            return GameDao(id, name, url, createdAt, updatedAt, summary, storyline, collectionId,
                    franchiseId, hypes, popularity, rating, ratingCount, aggregatedRating, aggregatedRatingCount,
                    totalRating, totalRatingCount, firstReleaseAt, mapFromTimeToBeatEntity(timeToBeat), mapFromCoverEntity(cover))
        }
    }

    private fun mapFromCoverModel(type: CoverDao?): CoverDomainModel? {
        type?.apply {
            return CoverDomainModel(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatModel(type: TimeToBeatDao?): TimeToBeatDomainModel? {
        type?.apply {
            return TimeToBeatDomainModel(hastly, normally, completely)
        }
        return null
    }

    private fun mapFromCoverEntity(type: CoverDomainModel?): CoverDao? {
        type?.apply {
            return CoverDao(url, width, height)
        }
        return null
    }

    private fun mapFromTimeToBeatEntity(type: TimeToBeatDomainModel?): TimeToBeatDao? {
        type?.apply {
            return TimeToBeatDao(hastly, normally, completely)
        }
        return null
    }
}