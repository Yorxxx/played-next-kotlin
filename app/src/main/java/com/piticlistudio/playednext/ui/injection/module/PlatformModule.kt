package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.PlatformMapper
import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.data.repository.PlatformRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.PlatformDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoService
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
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

    @Provides
    fun provideDTOMapper(mapper: PlatformMapper.DTOMapper): DTOModelMapper<PlatformDTO, Platform> = mapper

    @Provides
    fun provideDAOMapper(mapper: PlatformMapper.DaoMapper): DaoModelMapper<PlatformDao, Platform> = mapper

    @Provides
    @Named("dao")
    fun provideDatasource(dao: PlatformDaoRepositoryImpl): PlatformDatasourceRepository = dao
}