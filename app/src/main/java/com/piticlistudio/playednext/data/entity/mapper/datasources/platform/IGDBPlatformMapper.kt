package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.igdb.IGDBPlatform
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

/**
 * Mapper for [IGDBPlatform] and [Platform] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into IGDB models
 */
class IGDBPlatformMapper @Inject constructor() : DataLayerMapper<IGDBPlatform, Platform> {

    override fun mapFromDataLayer(model: IGDBPlatform): Platform = Platform(model.id, model.name, model.url, model.created_at, model.updated_at)
}