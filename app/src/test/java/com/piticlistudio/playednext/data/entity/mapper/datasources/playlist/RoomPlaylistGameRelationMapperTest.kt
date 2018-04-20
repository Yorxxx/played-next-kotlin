package com.piticlistudio.playednext.data.entity.mapper.datasources.playlist

import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RoomPlaylistGameRelationMapperTest {

    private lateinit var mapper: RoomPlaylistGameRelationMapper

    @BeforeEach
    internal fun setUp() {
        mapper = RoomPlaylistGameRelationMapper()
    }

    @Test
    fun `should map a Playlist entity into a RoomPlaylistGameRelation`() {

        val playlist = makePlaylist()

        val result = mapper.mapIntoDataLayerModel(playlist)

        assertNotNull(result)
        assertEquals(playlist.games.size, result.size)
        playlist.games.forEachIndexed { index, game ->
            assertEquals(game.id, result[index].gameId)
            assertEquals(playlist.name, result[index].playlistName)
            assertEquals(playlist.createdAt, result[index].created_at)
            assertEquals(playlist.updatedAt, result[index].updated_at)
        }
    }
}