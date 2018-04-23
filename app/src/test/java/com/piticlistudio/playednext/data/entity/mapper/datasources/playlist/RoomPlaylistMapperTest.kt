package com.piticlistudio.playednext.data.entity.mapper.datasources.playlist

import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makeRoomPlaylist
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RoomPlaylistMapperTest {

    private lateinit var mapper: RoomPlaylistMapper

    @BeforeEach
    internal fun setUp() {
        mapper = RoomPlaylistMapper()
    }

    @Test
    fun `given a RoomPlaylistEntity, should map into Playlist without games`() {
//        val gameId = randomInt()
        val roomPlaylist = makeRoomPlaylist()

        val result = mapper.mapFromDataLayer(roomPlaylist)

        assertNotNull(result)
        with(result) {
            assertEquals(roomPlaylist.name, name)
            assertEquals(roomPlaylist.color, color)
            assertEquals(roomPlaylist.created_at, createdAt)
            assertEquals(roomPlaylist.updated_at, updatedAt)
            assertEquals(roomPlaylist.description, description)
            assertTrue(games.isEmpty())
        }
    }

    @Test
    fun `given a Playlist, should map into RoomPlaylistEntity`() {
        val playlist = makePlaylist()

        val result = mapper.mapIntoDataLayerModel(playlist)

        assertNotNull(result)
        with(result) {
            assertEquals(playlist.name, name)
            assertEquals(playlist.color, color)
            assertEquals(playlist.createdAt, created_at)
            assertEquals(playlist.updatedAt, updated_at)
            assertEquals(playlist.description, description)
        }

    }
}