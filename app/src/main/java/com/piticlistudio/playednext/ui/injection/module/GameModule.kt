package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.ImageMapper
import com.piticlistudio.playednext.data.entity.net.GameDTO
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameLocalImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.repository.GameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class GameModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): GameDaoService {
        return db.gamesDao()
    }

    @Provides
    @Singleton
    fun provideGameRepository(repository: GameRepositoryImpl): GameRepository {
        return repository
    }

    @Provides
    fun provideDTOMapper(mapper: GameDTOMapper): DTOModelMapper<GameDTO, Game> = mapper

    @Provides
    @Named("dao")
    fun provideDatasource(dao: GameLocalImpl): GameDatasourceRepository = dao

    @Provides
    fun provideDAOMapper(mapper: GameDaoMapper): DaoModelMapper<GameDao, Game> = mapper
}