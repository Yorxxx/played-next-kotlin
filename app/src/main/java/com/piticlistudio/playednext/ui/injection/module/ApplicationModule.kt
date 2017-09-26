package com.piticlistudio.playednext.ui.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.game.mapper.GameEntityMapper
import com.piticlistudio.playednext.data.game.mapper.local.GameDaoMapper
import com.piticlistudio.playednext.data.game.mapper.remote.IGDBGameMapper
import com.piticlistudio.playednext.data.game.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.game.repository.local.GameDao
import com.piticlistudio.playednext.data.game.repository.local.GameLocalImpl
import com.piticlistudio.playednext.data.game.repository.remote.GameRemoteImpl
import com.piticlistudio.playednext.data.game.repository.remote.GameService
import com.piticlistudio.playednext.data.remote.GameServiceFactory
import com.piticlistudio.playednext.domain.repository.game.GameRepository
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
    internal fun provideGameService(): GameService {
        return GameServiceFactory.makeGameService()
    }

    @Provides
    @Singleton
    fun provideGamesDao(db: AppDatabase): GameDao {
        return db.gamesDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(ctx: Context): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "my-todo-db").build()
    }

    @Provides
    @Singleton
    fun provideGameLocalRepository(dao: GameDao, mapper: GameDaoMapper): GameLocalImpl {
        return GameLocalImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideGameRemoteRepository(service: GameService, mapper: IGDBGameMapper): GameRemoteImpl {
        return GameRemoteImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideGameRepositor(localImpl: GameLocalImpl, remoteImpl: GameRemoteImpl, mapper: GameEntityMapper): GameRepository {
        return GameRepositoryImpl(remoteImpl, localImpl, mapper)
    }
}
