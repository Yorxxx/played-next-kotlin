package com.piticlistudio.playednext.ui.playlists.overview.mapper

import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.ui.playlists.overview.model.PlaylistsOverviewModel
import javax.inject.Inject

/**
 * A mapper for converting a [Playlist] business entity into a [PlaylistsOverviewModel] presentation model.
 */
class PlaylistsOverviewModelMapper @Inject constructor() {

    fun mapIntoPresentationModel(model: Playlist): PlaylistsOverviewModel = PlaylistsOverviewModel(model.name, model.games.size, model.color)
}