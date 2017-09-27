package com.piticlistudio.playednext.data.game.mapper

import com.piticlistudio.playednext.data.EntityMapper
import com.piticlistudio.playednext.data.game.model.GameEntity
import com.piticlistudio.playednext.domain.model.game.Game
import javax.inject.Inject

/**
 * Maps a [GameEntity] into a [Game] entity
 */
open class GameEntityMapper @Inject constructor() : EntityMapper<GameEntity, Game> {

    override fun mapFromModel(type: GameEntity): Game {
        return Game(type.id, type.name, type.summary, type.storyline)
    }

    fun mapFromDomain(data: Game): GameEntity {
        with(data) {
            return GameEntity(id, name, summary, storyline)
        }
    }
}