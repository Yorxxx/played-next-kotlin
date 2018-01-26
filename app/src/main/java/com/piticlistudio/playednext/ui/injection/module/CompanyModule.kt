package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyMapper
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.data.repository.CompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoService
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class CompanyModule {

    @Provides
    @Singleton
    fun provideCompanyDao(db: AppDatabase): CompanyDaoService {
        return db.companyDao()
    }

    @Provides
    @Singleton
    fun provideCompanyRepository(repository: CompanyRepositoryImpl): CompanyRepository {
        return repository
    }

    @Provides
    fun provideCompanyDTOMapper(mapper: CompanyMapper.DTOMapper): DTOModelMapper<CompanyDTO, Company> = mapper

    @Provides
    fun provideCompanyDAOMapper(mapper: CompanyMapper.DaoMapper): DaoModelMapper<CompanyDao, Company> = mapper

    @Provides
    @Named("dao")
    fun provideCompanyDaoRepository(dao: CompanyDaoRepositoryImpl): CompanyDatasourceRepository = dao
}