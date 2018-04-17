package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.room.RoomPlatform
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombPlatform
import com.piticlistudio.playednext.data.entity.igdb.IGDBPlatform
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class PlatformFactory {

    companion object Factory {

        fun makeRoomPlatform(): RoomPlatform {
            return RoomPlatform(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makePlatform(id: Int = randomInt(), name: String = randomString()): Platform = Platform(id, name, DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())

        fun makeIGDBPlatform(): IGDBPlatform = IGDBPlatform(randomInt(), randomString(), randomString(), randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())

        fun makeGiantbombPlatform(): GiantbombPlatform = GiantbombPlatform(randomInt(), randomString(), randomString(), randomString())
    }
}