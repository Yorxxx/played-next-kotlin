package com.piticlistudio.playednext.data.entity.mapper

import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.domain.model.game.Game
import javax.inject.Inject

/**
 * Maps a [GameEntity] into a [Game] entity
 */
open class GameEntityToDomainMapper @Inject constructor() : LayerDataMapper<GameEntity, Game> {

    override fun mapFromModel(type: GameEntity): Game {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapFromEntity(type: Game): GameEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}