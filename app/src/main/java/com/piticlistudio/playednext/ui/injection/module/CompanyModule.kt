package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDaoMapper
import com.piticlistudio.playednext.data.repository.CompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.net.CompanyRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CompanyModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): CompanyDaoService {
        return db.companyDao()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(dao: CompanyDaoService, mapper: CompanyDaoMapper): CompanyDaoRepositoryImpl {
        return CompanyDaoRepositoryImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(service: IGDBService, mapper: CompanyDTOMapper): CompanyRemoteImpl {
        return CompanyRemoteImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideCompanyRepository(localImpl: CompanyDaoRepositoryImpl, remoteImpl: CompanyRemoteImpl): CompanyRepository {
        return CompanyRepositoryImpl(localImpl, remoteImpl)
    }
}