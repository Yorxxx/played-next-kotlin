package com.piticlistudio.playednext.data.entity.mapper.datasources.game

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.GiantbombGame
import com.piticlistudio.playednext.domain.model.Game

/**
 * Mapper for converting [GiantbombGame] into [Game]
 * Created by e-jegi on 3/26/2018.
 */
class GiantbombMapper : LayerDataMapper<GiantbombGame, Game>{

    override fun mapFromModel(type: GiantbombGame): Game {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapFromEntity(type: Game): GiantbombGame {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}