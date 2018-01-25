package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.domain.model.GameImage
import javax.inject.Inject

/**
 * Mapper for image entities
 */
class ImageMapper {


    class DaoMapper @Inject constructor() : DaoModelMapper<ScreenshotDao, GameImage> {

        override fun mapFromDao(dao: ScreenshotDao): GameImage {
            with(dao) {
                return GameImage(id, url, width, height)
            }
        }

        override fun mapIntoDao(entity: GameImage): ScreenshotDao {
            with(entity) {
                return ScreenshotDao(id, null, url, width, height)
            }
        }
    }

    class DTOMapper @Inject constructor() : DTOModelMapper<ImageDTO, GameImage> {

        override fun mapFromDTO(dto: ImageDTO): GameImage {
            with(dto) {
                return GameImage(cloudinary_id!!, url, width, height)
            }
        }
    }
}