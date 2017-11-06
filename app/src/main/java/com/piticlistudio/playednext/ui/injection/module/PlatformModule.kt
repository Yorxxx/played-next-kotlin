package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.PlatformRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoService
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlatformModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): PlatformDaoService {
        return db.platformDao()
    }

    @Provides
    @Singleton
    fun provideRepository(repository: PlatformRepositoryImpl): PlatformRepository {
        return repository
    }
}