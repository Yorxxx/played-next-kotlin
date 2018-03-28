package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameServiceFactory
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.data.repository.datasource.net.giantbomb.GiantbombGameDatasourceRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.giantbomb.GiantbombService
import com.piticlistudio.playednext.data.repository.datasource.net.giantbomb.GiantbombServiceFactory
import com.piticlistudio.playednext.domain.repository.GameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class GameModule {

    @Provides
    @Singleton
    fun provideGiantbombService(): GiantbombService {
        return GiantbombServiceFactory.makeGameService()
    }

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): GameDaoService {
        return db.gamesDao()
    }

    @Provides
    @Singleton
    fun provideGameRepository(repository: GameRepositoryImpl): GameRepository = repository

    @Provides
    @Singleton
    @Named("giantbomb")
    fun provideGiantbombRepository(repository: GiantbombGameDatasourceRepositoryImpl): GameDatasourceRepository = repository

    @Provides
    @Singleton
    @Named("room")
    fun provideRoomRepository(repository: GameLocalImpl): GameDatasourceRepository = repository
}