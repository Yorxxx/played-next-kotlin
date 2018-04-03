package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.igdb.ImageDTO
import com.piticlistudio.playednext.domain.model.GameImage
import javax.inject.Inject

class ImageDTOMapper @Inject constructor() : LayerDataMapper<ImageDTO, GameImage> {

    override fun mapFromModel(type: ImageDTO): GameImage {
        with(type) {
            return GameImage(cloudinary_id!!, url, width, height)
        }
    }

    override fun mapFromEntity(type: GameImage): ImageDTO {
        with(type) {
            return ImageDTO(url, id, width, height)
        }
    }
}