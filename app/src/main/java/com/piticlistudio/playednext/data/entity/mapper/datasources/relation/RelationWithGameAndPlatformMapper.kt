package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.dao.RelationWithGameAndPlatform
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

/**
 * Mapper for converting [RelationWithGameAndPlatform] into [GameRelation]
 * Created by e-jegi on 25/01/2018.
 */
class RelationWithGameAndPlatformMapper @Inject constructor(private val gamemapper: DaoModelMapper<GameDao, Game>,
                                                            private val platformmapper: DaoModelMapper<PlatformDao, Platform>) : DaoModelMapper<RelationWithGameAndPlatform, GameRelation> {

    override fun mapFromDao(dao: RelationWithGameAndPlatform): GameRelation {
        assert(dao.game != null && dao.game!!.size == 1)
        assert(dao.platform != null && dao.platform!!.size == 1)
        val game = gamemapper.mapFromDao(dao.game!!.first())
        val platform = platformmapper.mapFromDao(dao.platform!!.first())
        return GameRelation(game, platform, GameRelationStatus.values().get(dao.data!!.status), dao.data!!.created_at, dao.data!!.updated_at)
    }

    override fun mapIntoDao(entity: GameRelation): RelationWithGameAndPlatform {
        return RelationWithGameAndPlatform().apply {
            game = listOf(gamemapper.mapIntoDao(entity.game!!))
            platform = listOf(platformmapper.mapIntoDao(entity.platform!!))
            data = GameRelationDao(entity.game!!.id, entity.platform!!.id, entity.currentStatus.ordinal,
                    entity.createdAt, entity.updatedAt)
        }
    }
}