package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.room.image.RoomGameImagesService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameImagesModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomGameImagesService {
        return db.imageRoom()
    }
}