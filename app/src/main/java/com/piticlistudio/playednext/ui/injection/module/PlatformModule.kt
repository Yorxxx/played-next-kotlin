package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreDaoMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDaoMapper
import com.piticlistudio.playednext.data.repository.GenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.PlatformRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoService
import com.piticlistudio.playednext.data.repository.datasource.net.GenreRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.data.repository.datasource.net.platform.PlatformDTORepositoryImpl
import com.piticlistudio.playednext.domain.repository.GenreRepository
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
    fun provideLocalRepository(dao: PlatformDaoService, mapper: PlatformDaoMapper): PlatformDaoRepositoryImpl {
        return PlatformDaoRepositoryImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(service: IGDBService, mapper: PlatformDTOMapper): PlatformDTORepositoryImpl {
        return PlatformDTORepositoryImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideRepository(localImpl: PlatformDaoRepositoryImpl, remoteImpl: PlatformDTORepositoryImpl): PlatformRepository {
        return PlatformRepositoryImpl(localImpl, remoteImpl)
    }
}