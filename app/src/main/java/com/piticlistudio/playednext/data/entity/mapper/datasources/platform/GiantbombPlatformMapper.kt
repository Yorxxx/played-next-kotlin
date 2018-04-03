package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.net.GiantbombPlatform
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class GiantbombPlatformMapper @Inject constructor() : DataLayerMapper<GiantbombPlatform, Platform> {

    override fun mapFromDataLayer(model: GiantbombPlatform): Platform {
        return Platform(id = model.id,
                name = model.name,
                url = model.site_detail_url)
                .apply {
                    displayName = model.abbreviation
                }
    }
}