package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreMapper
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.data.repository.GenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoService
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.Genre
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

    @Provides
    fun provideDTOMapper(mapper: GenreMapper.DTOMapper): DTOModelMapper<GenreDTO, Genre> = mapper

    @Provides
    fun provideDAOMapper(mapper: GenreMapper.DaoMapper): DaoModelMapper<GenreDao, Genre> = mapper
}