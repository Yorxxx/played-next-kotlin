package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyMapper
import com.piticlistudio.playednext.data.entity.net.CollectionDTO
import com.piticlistudio.playednext.data.repository.CollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoService
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.domain.model.Company
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

    @Provides
    fun provideDTOMapper(mapper: CollectionMapper.DTOMapper): DTOModelMapper<CollectionDTO, Collection> = mapper

    @Provides
    fun provideDAOMapper(mapper: CollectionMapper.DaoMapper): DaoModelMapper<CollectionDao, Collection> = mapper
}