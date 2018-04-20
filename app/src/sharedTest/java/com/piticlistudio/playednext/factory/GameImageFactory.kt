package com.piticlistudio.playednext.factory

import com.piticlistudio.playednext.data.entity.room.RoomGameImage
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGameImage
import com.piticlistudio.playednext.data.entity.igdb.IGDBImage
import com.piticlistudio.playednext.data.entity.room.RoomImage
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.model.Image
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomString

class GameImageFactory {

    companion object Factory {

        fun makeRoomGameImage(gameId: Int = randomInt()): RoomGameImage {
            return RoomGameImage(makeRoomImage(), randomInt(), gameId)
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