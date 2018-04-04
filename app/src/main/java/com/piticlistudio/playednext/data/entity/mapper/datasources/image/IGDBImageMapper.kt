package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.igdb.IGDBImage
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.model.Image
import javax.inject.Inject

/**
 * Mapper for [IGDBImage] and [GameImage] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into IGDB models
 */
class IGDBImageMapper @Inject constructor() : DataLayerMapper<IGDBImage, Image> {

    override fun mapFromDataLayer(model: IGDBImage): Image = Image(model.mediumSizeUrl, model.width, model.height)
}