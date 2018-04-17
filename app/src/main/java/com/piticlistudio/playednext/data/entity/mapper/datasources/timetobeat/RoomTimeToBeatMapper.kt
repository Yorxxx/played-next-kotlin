package com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomTimeToBeat
import com.piticlistudio.playednext.domain.model.TimeToBeat
import javax.inject.Inject

/**
 * Mapper for converting [RoomTimeToBeat] and [TimeToBeat]
 * This mapperIGDB implements both [DataLayerMapper] and [DomainLayerMapper], which allows to map entities
 * from datalayer into domain layer, and viceversa
 */
class RoomTimeToBeatMapper @Inject constructor() : DataLayerMapper<RoomTimeToBeat, TimeToBeat>, DomainLayerMapper<TimeToBeat, RoomTimeToBeat> {

    override fun mapFromDataLayer(model: RoomTimeToBeat): TimeToBeat = TimeToBeat(model.hastly, model.normally, model.completely)

    override fun mapIntoDataLayerModel(model: TimeToBeat): RoomTimeToBeat = RoomTimeToBeat(model.quick, model.normally, model.completely)
}