package com.piticlistudio.playednext.data.game.mapper.local

import com.piticlistudio.playednext.data.EntityMapper
import com.piticlistudio.playednext.data.game.model.GameEntity
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import javax.inject.Inject

/**
 * Mapper between [LocalGame] and [GameEntity]
 */
class GameDaoMapper @Inject constructor() : EntityMapper<LocalGame, GameEntity> {

    override fun mapFromModel(type: LocalGame): GameEntity {
        with(type) {
            return GameEntity(id.toInt(), name, summary, storyline, collection, franchise, rating)
        }
    }

    fun mapFromEntity(entity: GameEntity): LocalGame {
        with(entity) {
            return LocalGame(id.toLong(), name, summary, storyline, collection, franchise, rating);
        }
    }
}