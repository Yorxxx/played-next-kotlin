package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.room.genre.RoomGenreService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GenreModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomGenreService {
        return db.genreRoom()
    }
}