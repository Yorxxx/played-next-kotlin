package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.data.repository.CompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoService
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.model.Game
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
    fun provideCompanyRepository(repository: CompanyRepositoryImpl): CompanyRepository {
        return repository
    }

    @Provides
    fun provideDTOMapper(mapper: CompanyMapper.DTOMapper): DTOModelMapper<CompanyDTO, Company> = mapper

    @Provides
    fun provideDAOMapper(mapper: CompanyMapper.DaoMapper): DaoModelMapper<CompanyDao, Company> = mapper
}