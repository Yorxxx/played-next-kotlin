package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CollectionModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomCollectionService {
        return db.collectionRoom()
    }
}