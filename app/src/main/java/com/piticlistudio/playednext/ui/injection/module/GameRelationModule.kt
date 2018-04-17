package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.GameRelationRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.relation.RoomRelationRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.relation.RoomRelationService
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameRelationModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RoomRelationService = db.relationDao()

    @Provides
    @Singleton
    fun provideGameRelationRepository(repositoryImpl: GameRelationRepositoryImpl): GameRelationRepository = repositoryImpl

    @Provides
    @Singleton
    fun provideLocalDatasource(datasource: RoomRelationRepositoryImpl): RelationDatasourceRepository = datasource
}