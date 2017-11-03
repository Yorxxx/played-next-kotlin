package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import javax.inject.Inject

class RelationDaoMapper @Inject constructor() : LayerDataMapper<GameRelationDao, GameRelation> {

    override fun mapFromModel(type: GameRelationDao): GameRelation {
        with(type) {
            return GameRelation(null, null, GameRelationStatus.values().get(status), created_at, updated_at)
        }
    }

    override fun mapFromEntity(type: GameRelation): GameRelationDao {
        with(type) {
            return GameRelationDao(game!!.id, platform!!.id, currentStatus.ordinal, createdAt, updatedAt)
        }
    }
}