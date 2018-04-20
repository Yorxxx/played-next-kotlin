package com.piticlistudio.playednext.factory

import com.piticlistudio.playednext.data.entity.room.RoomPlaylist
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomString

class PlaylistFactory {

    companion object Factory {

        fun makeRoomPlaylist(name: String = randomString()): RoomPlaylist {
            return RoomPlaylist(name, randomString(), randomInt(), randomLong(), randomLong())
        }
    }
}