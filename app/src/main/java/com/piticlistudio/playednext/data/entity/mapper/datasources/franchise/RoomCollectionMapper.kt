package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomCollection
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject


/**
 * Mapper for converting [RoomCollection] and [Collection]
 * This mapperIGDB implements both [DataLayerMapper] and [DomainLayerMapper], which allows to map entities
 * from datalayer into domain layer, and viceversa
 */
class RoomCollectionMapper @Inject constructor() : DataLayerMapper<RoomCollection, Collection>, DomainLayerMapper<Collection, RoomCollection> {

    override fun mapFromDataLayer(model: RoomCollection): Collection = Collection(model.id, model.name, model.url)

    override fun mapIntoDataLayerModel(model: Collection): RoomCollection = RoomCollection(model.id, model.name, model.url)
}