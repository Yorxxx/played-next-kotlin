package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlatform
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class RoomPlatformMapper @Inject constructor() : DataLayerMapper<RoomPlatform, Platform>, DomainLayerMapper<Platform, RoomPlatform> {

    override fun mapFromDataLayer(model: RoomPlatform): Platform = Platform(model.id, model.name, model.url, model.created_at, model.updated_at).apply {
        this.displayName = model.abbreviation
    }

    override fun mapIntoDataLayerModel(model: Platform): RoomPlatform = RoomPlatform(model.id, model.name, model.displayName, model.url, model.createdAt, model.updatedAt)
}