package com.piticlistudio.playednext.data.entity.mapper.datasources.relation

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.RoomGameMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.RoomPlatformMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import com.piticlistudio.playednext.data.entity.room.RoomGameRelationProxy
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import javax.inject.Inject

/**
 * Mapper for converting [RoomGameRelationProxy] and [GameRelation]
 * This mapperIGDB implements both [DataLayerMapper] and [DomainLayerMapper], which allows to map entities
 * from datalayer into domain layer, and viceversa
 */
class RoomRelationMapper @Inject constructor(val gameMapper: RoomGameMapper,
                                             val platformMapper: RoomPlatformMapper) : DataLayerMapper<RoomGameRelationProxy, GameRelation>, DomainLayerMapper<GameRelation, RoomGameRelationProxy> {

    override fun mapFromDataLayer(model: RoomGameRelationProxy): GameRelation {
        return GameRelation(createdAt = model.relation.created_at,
                updatedAt = model.relation.updated_at,
                game = gameMapper.mapFromDataLayer(model.game),
                platform = platformMapper.mapFromDataLayer(model.platform),
                status = GameRelationStatus.values().get(model.relation.status)
        )
    }

    override fun mapIntoDataLayerModel(model: GameRelation): RoomGameRelationProxy {
        return RoomGameRelationProxy(game = gameMapper.mapIntoDataLayerModel(model.game),
                platform = platformMapper.mapIntoDataLayerModel(model.platform),
                relation = RoomGameRelation(model.game.id, model.platform.id, model.status.ordinal, model.createdAt, model.updatedAt))
    }
}