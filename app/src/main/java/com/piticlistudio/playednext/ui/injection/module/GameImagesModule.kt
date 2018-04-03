package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.GameImagesRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.image.GameImagesDaoService
import com.piticlistudio.playednext.domain.repository.GameImagesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameImagesModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): GameImagesDaoService {
        return db.imagesDao()
    }

    @Provides
    @Singleton
    fun provideRepository(repository: GameImagesRepositoryImpl): GameImagesRepository {
        return repository
    }
}