package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.CollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoService
import com.piticlistudio.playednext.domain.repository.CollectionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CollectionModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): CollectionDaoService {
        return db.collectionDao()
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(repository: CollectionRepositoryImpl): CollectionRepository {
        return repository
    }
}