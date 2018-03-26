package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.net.GiantbombPlatform
import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.domain.model.Platform

class PlatformFactory {

    companion object Factory {

        fun makePlatformDao(): PlatformDao {
            return PlatformDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makePlatform(): Platform {
            return Platform(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makePlatformDTO(): PlatformDTO {
            return PlatformDTO(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeGiantbombPlatform(): GiantbombPlatform = GiantbombPlatform(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString())
    }
}