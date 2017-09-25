package com.piticlistudio.playednext.data.game.mapper

import com.piticlistudio.playednext.data.EntityMapper
import com.piticlistudio.playednext.data.game.model.GameModel
import com.piticlistudio.playednext.domain.model.game.Game

/**
 * Maps a [GameModel] into a [Game] entity
 */
open class GameEntityMapper: EntityMapper<GameModel, Game> {

    override fun mapFromRemote(type: GameModel): Game {
        return Game(type.id, type.name, type.summary, type.storyline)
    }
}