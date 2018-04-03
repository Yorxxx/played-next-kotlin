package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.GenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoService
import com.piticlistudio.playednext.domain.repository.GenreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GenreModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): GenreDaoService {
        return db.genreDao()
    }

    @Provides
    @Singleton
    fun provideGenreRepository(repository: GenreRepositoryImpl): GenreRepository {
        return repository
    }
}