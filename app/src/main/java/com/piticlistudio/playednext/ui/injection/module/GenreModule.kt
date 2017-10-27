package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreDaoMapper
import com.piticlistudio.playednext.data.repository.GenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoService
import com.piticlistudio.playednext.data.repository.datasource.net.GenreRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
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
    fun provideLocalRepository(dao: GenreDaoService, mapper: GenreDaoMapper): GenreDaoRepositoryImpl {
        return GenreDaoRepositoryImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(service: IGDBService, mapper: GenreDTOMapper): GenreRemoteImpl {
        return GenreRemoteImpl(service, mapper)
    }

    @Provides
    @Singleton
    fun provideGenreRepository(localImpl: GenreDaoRepositoryImpl, remoteImpl: GenreRemoteImpl): GenreRepository {
        return GenreRepositoryImpl(localImpl, remoteImpl)
    }
}