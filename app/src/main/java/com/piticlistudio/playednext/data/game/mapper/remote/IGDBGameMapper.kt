package com.piticlistudio.playednext.data.game.mapper.remote

import com.piticlistudio.playednext.data.EntityMapper
import com.piticlistudio.playednext.data.game.model.GameModel
import com.piticlistudio.playednext.data.game.model.remote.IGDBGameModel

/**
 * Maps a [IGDBGame] into a [GameModel]
 */
class IGDBGameMapper : EntityMapper<IGDBGameModel, GameModel> {

    override fun mapFromRemote(type: IGDBGameModel): GameModel {
        return GameModel(type.id, type.name, type.summary, type.storyline, type.collection, type.franchise, type.rating)
    }
}