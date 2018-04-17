package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.room.RoomGameImage
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGameImage
import com.piticlistudio.playednext.data.entity.igdb.IGDBImage
import com.piticlistudio.playednext.data.entity.room.RoomImage
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.model.Image
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class GameImageFactory {

    companion object Factory {

        fun makeRoomGameImage(): RoomGameImage {
            return RoomGameImage(makeRoomImage(), randomInt(), randomInt())
        }

        fun makeRoomImage(): RoomImage = RoomImage(randomString(), randomInt(), randomInt())

        fun makeGameImage(): GameImage {
            return GameImage(randomString(), DataFactory.randomInt(), randomInt(), randomInt())
        }

        fun makeImage(): Image = Image(randomString(), randomInt(), randomInt())

        fun makeIGDBImage(id: String? = randomString()): IGDBImage {
            return IGDBImage(randomString(), id, randomInt(), randomInt())
        }

        fun makeGiantbombGameImage(): GiantbombGameImage {
            return GiantbombGameImage(randomString(), randomString(), randomString(), randomString(),
                    randomString(), randomString(), randomString(), randomString(), randomString())
        }
    }
}