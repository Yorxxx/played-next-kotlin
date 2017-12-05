package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class GameImageFactory {

    companion object Factory {

        fun makeGameImageDao(): ScreenshotDao {
            return ScreenshotDao(randomString(), DataFactory.randomInt(), DataFactory.randomString(), randomInt(), randomInt())
        }

        fun makeGameImage(): GameImage {
            return GameImage(randomString(), randomString(), DataFactory.randomInt(), randomInt())
        }

        fun makeImageDTO(): ImageDTO {
            return ImageDTO(randomString(), DataFactory.randomString(), randomInt(), randomInt())
        }
    }
}