package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.GameWithRelationalData
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.domain.model.Game
import javax.inject.Inject

/**
 * Maps between [GameWithRelationalData] and [Game] entities
 * Created by e-jegi on 25/01/2018.
 */
class GameWithRelationDaoMapper @Inject constructor(): DaoModelMapper<GameWithRelationalData, Game> {

    override fun mapFromDao(dao: GameWithRelationalData): Game {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapIntoDao(entity: Game): GameWithRelationalData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}