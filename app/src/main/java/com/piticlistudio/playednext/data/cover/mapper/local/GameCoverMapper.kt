package com.piticlistudio.playednext.data.cover.mapper.local

import com.piticlistudio.playednext.data.EntityMapper
import com.piticlistudio.playednext.data.cover.model.CoverEntity
import com.piticlistudio.playednext.data.cover.model.local.GameCover

/**
 * Class that maps between [GameCover] and [CoverEntity]
 */
class GameCoverMapper : EntityMapper<GameCover, CoverEntity> {

    override fun mapFromModel(type: GameCover): CoverEntity {
        with(type) {
            return CoverEntity(url, width, height, gameId)
        }
    }
}