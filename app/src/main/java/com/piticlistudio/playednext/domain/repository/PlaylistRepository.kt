package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Playlist
import io.reactivex.Flowable

/**
 * Interface defining methods for the playlists. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface PlaylistRepository {

    fun loadAll(): Flowable<List<Playlist>>
}