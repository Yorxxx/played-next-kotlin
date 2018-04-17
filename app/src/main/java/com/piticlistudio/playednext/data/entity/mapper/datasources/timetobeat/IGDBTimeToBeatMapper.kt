package com.piticlistudio.playednext.data.entity.mapper.datasources.timetobeat

import com.piticlistudio.playednext.data.entity.igdb.IGDBTimeToBeat
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.domain.model.TimeToBeat
import javax.inject.Inject


/**
 * Mapper for [IGDBTimeToBeat] and [TimeToBeat] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into IGDB models
 */
class IGDBTimeToBeatMapper @Inject constructor() : DataLayerMapper<IGDBTimeToBeat, TimeToBeat> {

    override fun mapFromDataLayer(model: IGDBTimeToBeat): TimeToBeat = TimeToBeat(model.hastly, model.normally, model.completely)
}