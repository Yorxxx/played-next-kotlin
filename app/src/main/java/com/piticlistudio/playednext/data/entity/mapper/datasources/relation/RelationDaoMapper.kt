package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.EntityDataMapper
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import javax.inject.Inject

class RelationDaoMapper @Inject constructor() : DaoModelMapper<GameRelationDao, GameRelation> {

    override fun mapFromDao(dao: GameRelationDao): GameRelation {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapIntoDao(entity: GameRelation): GameRelationDao {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun mapFromModel(type: GameRelationDao): GameRelation {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun mapFromEntity(type: GameRelation): GameRelationDao {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}