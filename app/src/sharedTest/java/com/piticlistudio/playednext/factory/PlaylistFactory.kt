package com.piticlistudio.playednext.factory

import com.piticlistudio.playednext.data.entity.room.RoomPlaylist
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame

class PlaylistFactory {

    companion object Factory {

        fun makeRoomPlaylist(name: String = randomString()): RoomPlaylist {
            return RoomPlaylist(name, randomString(), randomInt(), randomLong(), randomLong())
        }

        fun makePlaylist(): Playlist {
            return Playlist(name = randomString(),
                    description = randomString(),
                    color = randomInt(),
                    games = randomListOf(5){ makeGame() },
                    createdAt = randomLong(),
                    updatedAt = randomLong())
        }
    }
}