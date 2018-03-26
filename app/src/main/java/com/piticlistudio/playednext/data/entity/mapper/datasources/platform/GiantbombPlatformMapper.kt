package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.net.GiantbombPlatform
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class GiantbombPlatformMapper @Inject constructor() : DataLayerMapper<GiantbombPlatform, Platform> {

    override fun mapFromDataLayer(model: GiantbombPlatform): Platform {
        return Platform(model.id, model.name)
                .apply {
                    displayName = model.abbreviation
                }
    }
}