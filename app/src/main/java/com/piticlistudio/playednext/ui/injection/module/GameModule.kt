package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDaoService
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
    fun provideGameRepository(repository: GameRepositoryImpl): GameRepository {
        return repository
    }
}