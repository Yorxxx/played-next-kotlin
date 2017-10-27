package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.domain.repository.GameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): GameDaoService {
        return db.gamesDao()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(dao: GameDaoService, mapper: GameDaoMapper): GameLocalImpl {
        return GameLocalImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(service: IGDBService, mapper: GameDTOMapper): GameRemoteImpl {
        return GameRemoteImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideGameRepository(localImpl: GameLocalImpl, remoteImpl: GameRemoteImpl): GameRepository {
        return GameRepositoryImpl(remoteImpl, localImpl)
    }
}