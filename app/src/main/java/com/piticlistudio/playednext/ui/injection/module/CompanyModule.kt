package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.room.company.RoomCompanyService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CompanyModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomCompanyService {
        return db.companyRoom()
    }
}