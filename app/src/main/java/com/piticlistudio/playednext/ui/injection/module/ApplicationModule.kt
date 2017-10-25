package com.piticlistudio.playednext.ui.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.data.repository.datasource.net.GameServiceFactory
import com.piticlistudio.playednext.domain.repository.GameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app
    }

    @Provides
    @Singleton
    internal fun provideGameService(): IGDBService {
        return GameServiceFactory.makeGameService()
    }

    @Provides
    @Singleton
    fun provideGamesDao(db: AppDatabase): GameDaoService {
        return db.gamesDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(ctx: Context): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "my-todo-db").build()
    }

    @Provides
    @Singleton
    fun provideGameLocalRepository(dao: GameDaoService, mapper: GameDaoMapper): GameLocalImpl {
        return GameLocalImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideGameRemoteRepository(service: IGDBService, mapper: GameDTOMapper): GameRemoteImpl {
        return GameRemoteImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideGameRepository(localImpl: GameLocalImpl, remoteImpl: GameRemoteImpl): GameRepository {
        return GameRepositoryImpl(remoteImpl, localImpl)
    }
}
