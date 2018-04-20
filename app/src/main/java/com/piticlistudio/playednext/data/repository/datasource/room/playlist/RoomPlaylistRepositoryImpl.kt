package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylist
import com.piticlistudio.playednext.data.repository.datasource.PlaylistDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Playlist
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RoomPlaylistRepositoryImpl @Inject constructor(private val dao: RoomPlaylistService,
                                                     private val domainLayerMapper: DomainLayerMapper<Playlist, RoomPlaylist>,
                                                     private val dataLayerMapper: DataLayerMapper<RoomPlaylist, Playlist>,
                                                     private val roomGameRepositoryImpl: RoomGameRepositoryImpl): PlaylistDatasourceRepository {

    override fun save(data: Playlist): Completable {
        return Completable.defer {
            val mapped = domainLayerMapper.mapIntoDataLayerModel(data)
            if (dao.insert(mapped) == 0L) {
                Completable.error(PlaylistRepositoryError.Save())
            }
            else {
                Completable.complete()
            }
        }
    }

    override fun delete(data: Playlist): Completable {
        return Completable.defer {
            val mapped = domainLayerMapper.mapIntoDataLayerModel(data)
            if (dao.delete(mapped) == 0) {
                Completable.error(PlaylistRepositoryError.Delete())
            }
            else {
                Completable.complete()
            }
        }
    }

    override fun addGameToPlaylist(game: Game, playlist: Playlist): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeGameFromPlaylist(game: Game, playlist: Playlist): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): Flowable<List<Playlist>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(name: String): Flowable<Playlist> {
        return dao.find(name)
                .map { dataLayerMapper.mapFromDataLayer(it) }

    }
}

sealed class PlaylistRepositoryError: RuntimeException() {
    class Save: PlaylistRepositoryError()
    class Delete: PlaylistRepositoryError()
}