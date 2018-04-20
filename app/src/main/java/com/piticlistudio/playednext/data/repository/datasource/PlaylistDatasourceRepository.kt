package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Playlist
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Interface defining methods for the [Playlist]. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface PlaylistDatasourceRepository {

    /**
     * Inserts a new Playlist defined by [data]
     * @return a Completable when it succeeds or fails
     */
    fun save(data: Playlist): Completable

    /**
     * Deletes a playlist
     */
    fun delete(data: Playlist): Completable

    /**
     * Adds the [game] to the specified [playlist]
     */
    fun addGameToPlaylist(game: Game, playlist: Playlist): Completable

    /**
     * Removes the [game] from the specified [playlist]
     */
    fun removeGameFromPlaylist(game: Game, playlist: Playlist): Completable

    /**
     * Finds all stored playlists
     */
    fun findAll(): Flowable<List<Playlist>>

    /**
     * Returns a Flowable that emits a [Playlist] with the supplied [name]
     */
    fun find(name: String): Flowable<Playlist>
}