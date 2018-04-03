package com.piticlistudio.playednext.ui.injection.module

import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.GameRelationRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.relation.RelationDaoService
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GameRelationModule {

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): RelationDaoService = db.relationDao()

    @Provides
    @Singleton
    fun provideGameRelationRepository(repositoryImpl: GameRelationRepositoryImpl): GameRelationRepository = repositoryImpl
}