package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.GameImage
import javax.inject.Inject

class ImageDaoMapper @Inject constructor() : LayerDataMapper<ScreenshotDao, GameImage> {

    override fun mapFromModel(type: ScreenshotDao): GameImage {
        with(type) {
            return GameImage(id, url, width, height)
        }
    }

    override fun mapFromEntity(type: GameImage): ScreenshotDao {
        with(type) {
            return ScreenshotDao(id, null, url, width, height)
        }
    }
}

