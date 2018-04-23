package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.PlaylistDatasourceRepository
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.domain.repository.PlaylistRepository
import io.reactivex.Flowable
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(private val localImpl: PlaylistDatasourceRepository) : PlaylistRepository {

    override fun loadAll(): Flowable<List<Playlist>> = localImpl.findAll()
}