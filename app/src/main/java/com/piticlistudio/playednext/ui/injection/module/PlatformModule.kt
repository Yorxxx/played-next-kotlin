package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlatformModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomGamePlatformService {
        return db.platformRoom()
    }
}