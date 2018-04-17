package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGameImage
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.domain.model.Image
import javax.inject.Inject

class GiantbombImageMapper @Inject constructor() : DataLayerMapper<GiantbombGameImage, Image?> {

    override fun mapFromDataLayer(model: GiantbombGameImage): Image? {
        with(model) {
            when {
                model.medium_url != null -> return Image(model.medium_url)
                model.screen_url != null -> return Image(model.screen_url)
                model.screen_large_url != null -> return Image(model.screen_large_url)
                model.small_url != null -> return Image(model.small_url)
                model.original_url != null -> return Image(model.original_url)
                else -> return null
            }
        }
    }
}