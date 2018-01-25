package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.dao.ScreenshotDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.CollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.ImageMapper
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.data.entity.net.ImageDTO
import com.piticlistudio.playednext.data.repository.GameImagesRepositoryImpl
import com.piticlistudio.playednext.data.repository.PlatformRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.image.GameImagesDaoService
import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoService
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.domain.repository.GameImagesRepository
import com.piticlistudio.playednext.domain.repository.PlatformRepository
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

    @Provides
    fun provideDTOMapper(mapper: ImageMapper.DTOMapper): DTOModelMapper<ImageDTO, GameImage> = mapper

    @Provides
    fun provideDAOMapper(mapper: ImageMapper.DaoMapper): DaoModelMapper<ScreenshotDao, GameImage> = mapper
}