package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.playlist.RoomPlaylistMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.data.repository.PlaylistRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.PlaylistDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.gameplaylist.RoomGamePlaylistService
import com.piticlistudio.playednext.data.repository.datasource.room.playlist.RoomPlaylistRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.playlist.RoomPlaylistService
import com.piticlistudio.playednext.domain.model.Playlist
import com.piticlistudio.playednext.domain.repository.PlaylistRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlaylistModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomPlaylistService = db.playlistRoom()

    @Provides
    @Singleton
    fun provideRelationDao(db: AppDatabase): RoomGamePlaylistService = db.gamePlaylistRoom()

    @Provides
    @Singleton
    fun providePlaylistRepository(repositoryImpl: PlaylistRepositoryImpl): PlaylistRepository = repositoryImpl

    @Provides
    @Singleton
    fun provideLocalDatasource(datasource: RoomPlaylistRepositoryImpl): PlaylistDatasourceRepository = datasource

    @Provides
    fun provideDomainMapper(mapper: RoomPlaylistMapper): DomainLayerMapper<Playlist, RoomPlaylistEntity> = mapper

    @Provides
    fun provideDataMapper(mapper: RoomPlaylistMapper): DataLayerMapper<RoomPlaylistEntity, Playlist> = mapper
}