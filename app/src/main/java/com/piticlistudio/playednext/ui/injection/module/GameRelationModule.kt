package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.dao.RelationWithGameAndPlatform
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RelationWithGameAndPlatformMapper
import com.piticlistudio.playednext.data.repository.GameRelationRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.relation.RelationDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.relation.RelationDaoService
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module(
        includes = arrayOf(GameModule::class, PlatformModule::class)
)
class GameRelationModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RelationDaoService = db.relationDao()

    @Provides
    @Singleton
    fun provideGameRelationRepository(repositoryImpl: GameRelationRepositoryImpl): GameRelationRepository = repositoryImpl

    @Provides
    @Inject
    fun provideMapper(mapper: RelationWithGameAndPlatformMapper): DaoModelMapper<RelationWithGameAndPlatform, GameRelation> = mapper

    @Provides
    fun provideDaoDatasource(dao: RelationDaoRepositoryImpl): RelationDatasourceRepository = dao
}