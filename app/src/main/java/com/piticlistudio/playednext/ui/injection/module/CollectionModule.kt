package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.CollectionDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CollectionDaoMapper
import com.piticlistudio.playednext.data.repository.CollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoService
import com.piticlistudio.playednext.data.repository.datasource.net.CollectionDTORepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
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
    fun provideLocalRepository(dao: CollectionDaoService, mapper: CollectionDaoMapper): CollectionDaoRepositoryImpl {
        return CollectionDaoRepositoryImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(service: IGDBService, mapper: CollectionDTOMapper): CollectionDTORepositoryImpl {
        return CollectionDTORepositoryImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(localImpl: CollectionDaoRepositoryImpl, remoteImpl: CollectionDTORepositoryImpl): CollectionRepository {
        return CollectionRepositoryImpl(localImpl, remoteImpl)
    }
}