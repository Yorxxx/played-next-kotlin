package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelationEntity
import com.piticlistudio.playednext.data.repository.datasource.PlaylistDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.gameplaylist.RoomGamePlaylistService
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Playlist
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RoomPlaylistRepositoryImpl @Inject constructor(private val dao: RoomPlaylistService,
                                                     private val relationDao: RoomGamePlaylistService,
                                                     private val domainLayerMapper: DomainLayerMapper<Playlist, RoomPlaylistEntity>,
                                                     private val dataLayerMapper: DataLayerMapper<RoomPlaylistEntity, Playlist>,
                                                     private val roomGameRepositoryImpl: RoomGameRepositoryImpl) : PlaylistDatasourceRepository {

    override fun save(data: Playlist): Completable {
        return Completable.defer {
            dao.insert(domainLayerMapper.mapIntoDataLayerModel(data))
            Completable.complete()
        }.andThen(Completable.defer {
            data.games.forEach {
                relationDao.insert(RoomPlaylistGameRelationEntity(data.name, it.id))
            }
            Completable.complete()
        })
    }

    override fun delete(data: Playlist): Completable {
        return Completable.defer {
            dao.delete(domainLayerMapper.mapIntoDataLayerModel(data))
            Completable.complete()
        }
    }

    override fun saveGameToPlaylist(game: Game, playlist: Playlist): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteGameFromPlaylist(game: Game, playlist: Playlist): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): Flowable<List<Playlist>> {
        return dao.findAll()
                .flatMapSingle {
                    Flowable.fromIterable(it)
                            .map { dataLayerMapper.mapFromDataLayer(it) }
                            .flatMap { playlist: Playlist ->
                                relationDao.find(playlist.name)
                                        .flatMapIterable { it }
                                        .flatMap { roomGameRepositoryImpl.load(it.gameId) }
                                        .toList()
                                        .map { playlist.copy(games = it) }
                                        .toFlowable()
                            }
                            .toList()
                }
    }

    override fun find(name: String): Flowable<Playlist> {
        return dao.find(name)
                .map { dataLayerMapper.mapFromDataLayer(it) }
                .flatMap { playlist: Playlist ->
                    relationDao.find(playlist.name)
                            .flatMapIterable { it }
                            .flatMap { roomGameRepositoryImpl.load(it.gameId) }
                            .toList()
                            .map { playlist.copy(games = it) }
                            .toFlowable()
                }
    }
}