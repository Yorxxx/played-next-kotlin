package com.piticlistudio.playednext.factory

import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame

class PlaylistFactory {

    companion object Factory {

        fun makeRoomPlaylist(name: String = randomString()): RoomPlaylistEntity {
            return RoomPlaylistEntity(name, randomString(), randomInt(), randomLong(), randomLong())
        }

        fun makePlaylist(name: String = randomString(),
                         games: List<Game> = randomListOf(5){ makeGame() }): Playlist {
            return Playlist(name,
                    description = randomString(),
                    color = randomInt(),
                    games = games,
                    createdAt = randomLong(),
                    updatedAt = randomLong())
        }
    }
}