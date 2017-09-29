package com.piticlistudio.playednext.data.entity.mapper

import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.domain.model.game.Game
import javax.inject.Inject

/**
 * Maps a [GameDomainModel] into a [Game] entity
 */
open class GameEntityToDomainMapper @Inject constructor() : LayerDataMapper<GameDomainModel, Game> {

    override fun mapFromModel(type: GameDomainModel): Game {
        with(type) {
            return Game(id, name, summary, storyline)
        }
    }

    override fun mapFromEntity(type: Game): GameDomainModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}