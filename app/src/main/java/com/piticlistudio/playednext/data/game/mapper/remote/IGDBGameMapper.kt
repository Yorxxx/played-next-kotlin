package com.piticlistudio.playednext.data.game.mapper.remote

import com.piticlistudio.playednext.data.EntityMapper
import com.piticlistudio.playednext.data.game.model.GameEntity
import com.piticlistudio.playednext.data.game.model.remote.IGDBGameModel
import javax.inject.Inject

/**
 * Maps a [IGDBGame] into a [GameEntity]
 */
class IGDBGameMapper @Inject constructor() : EntityMapper<IGDBGameModel, GameEntity> {

    override fun mapFromRemote(type: IGDBGameModel): GameEntity {
        return GameEntity(type.id, type.name, type.summary, type.storyline, type.collection, type.franchise, type.rating)
    }
}