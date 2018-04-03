package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.CompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoService
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
}