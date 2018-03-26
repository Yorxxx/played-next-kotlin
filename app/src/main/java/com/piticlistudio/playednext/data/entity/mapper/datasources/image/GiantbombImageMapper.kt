package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.net.GiantbombGameImage
import com.piticlistudio.playednext.domain.model.GameImage
import javax.inject.Inject

class GiantbombImageMapper @Inject constructor() : DataLayerMapper<GiantbombGameImage, GameImage?> {

    override fun mapFromDataLayer(model: GiantbombGameImage): GameImage? {
        with(model) {
            when {
                model.medium_url != null -> return com.piticlistudio.playednext.domain.model.GameImage(model.medium_url, model.medium_url)
                model.screen_url != null -> return com.piticlistudio.playednext.domain.model.GameImage(model.screen_url, model.screen_url)
                model.screen_large_url != null -> return com.piticlistudio.playednext.domain.model.GameImage(model.screen_large_url, model.screen_large_url)
                model.small_url != null -> return com.piticlistudio.playednext.domain.model.GameImage(model.small_url, model.small_url)
                model.original_url != null -> return com.piticlistudio.playednext.domain.model.GameImage(model.original_url, model.original_url)
                else -> return null
            }
        }
    }
}