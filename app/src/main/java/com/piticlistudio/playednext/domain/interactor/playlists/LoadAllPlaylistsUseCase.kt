package com.piticlistudio.playednext.domain.interactor.playlists

import com.piticlistudio.playednext.domain.interactor.FlowableUseCase
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.domain.repository.PlaylistRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Usecase that allows to retrieve all available playlists from the data layer.
 */
class LoadAllPlaylistsUseCase @Inject constructor(private val repository: PlaylistRepository) : FlowableUseCase<List<Playlist>> {

    override fun execute(): Flowable<List<Playlist>> = repository.loadAll()
}