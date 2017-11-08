package com.piticlistudio.playednext.ui.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.net.GameServiceFactory
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app
    }

    @Provides
    @Singleton
    internal fun provideIGDBService(): IGDBService {
        return GameServiceFactory.makeGameService()
    }

    @Provides
    @Singleton
    fun provideDatabase(ctx: Context): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "my-todo-db").fallbackToDestructiveMigration().build()
    }
}
