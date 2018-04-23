package com.piticlistudio.playednext.ui.playlists.overview.mapper

import com.piticlistudio.playednext.factory.PlaylistFactory.Factory.makePlaylist
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PlaylistsOverviewModelMapperTest {

    private val mapper = PlaylistsOverviewModelMapper()

    @Test
    fun `mapIntoPresentationModel() should map a Playlist into a PlaylistsOverviewModel`() {
        val playlist = makePlaylist()

        val result = mapper.mapIntoPresentationModel(playlist)

        assertEquals(playlist.name, result.name)
        assertEquals(playlist.color, result.color)
        assertEquals(playlist.games.size, result.count)
    }
}