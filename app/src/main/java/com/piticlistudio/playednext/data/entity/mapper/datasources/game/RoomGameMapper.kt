package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat.RoomTimeToBeatMapper
import com.piticlistudio.playednext.data.entity.room.RoomGame
import com.piticlistudio.playednext.data.entity.room.RoomGameProxy
import com.piticlistudio.playednext.data.entity.room.RoomImage
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Image
import javax.inject.Inject

/**
 * Mapper for converting [RoomGameProxy] and [Game]
 * This mapperIGDB implements both [DataLayerMapper] and [DomainLayerMapper], which allows to map entities
 * from datalayer into domain layer, and viceversa
 */
class RoomGameMapper @Inject constructor(private val timeToBeatMapper: RoomTimeToBeatMapper) : DataLayerMapper<RoomGameProxy, Game>, DomainLayerMapper<Game, RoomGameProxy> {

    override fun mapFromDataLayer(model: RoomGameProxy): Game {
        with(model) {
            return Game(id = game.id,
                    name = game.name,
                    createdAt = game.createdAt,
                    updatedAt = game.updatedAt,
                    summary = game.summary,
                    storyline = game.storyline,
                    url = game.url,
                    rating = game.rating,
                    ratingCount = game.ratingCount,
                    aggregatedRating = game.agregatedRating,
                    aggregatedRatingCount = game.aggregatedRatingCount,
                    totalRating = game.totalRating,
                    totalRatingCount = game.totalRatingCount,
                    releasedAt = game.firstReleaseAt,
                    cover = game.cover?.let { Image(it.url, it.width, it.height) },
                    timeToBeat = game.timeToBeat?.let { timeToBeatMapper.mapFromDataLayer(it) },
                    developers = developers,
                    publishers = publishers,
                    genres = genres,
                    platforms = platforms,
                    syncedAt = game.syncedAt,
                    collection = collection,
                    images = images)
        }
    }

    override fun mapIntoDataLayerModel(model: Game): RoomGameProxy {
        with(model) {
            val game = RoomGame(id = id,
                    collection = collection?.id,
                    syncedAt = syncedAt,
                    timeToBeat = timeToBeat?.let { timeToBeatMapper.mapIntoDataLayerModel(it) },
                    cover = cover?.let { RoomImage(it.url, it.width, it.height) },
                    totalRatingCount = totalRatingCount,
                    totalRating = totalRating,
                    aggregatedRatingCount = aggregatedRatingCount,
                    ratingCount = ratingCount,
                    rating = rating,
                    url = url,
                    storyline = storyline,
                    summary = summary,
                    updatedAt = updatedAt,
                    createdAt = createdAt,
                    name = name,
                    popularity = null,
                    hypes = null,
                    franchise = null,
                    agregatedRating = aggregatedRating,
                    firstReleaseAt = releasedAt)
            return RoomGameProxy(game = game)
        }
    }
}