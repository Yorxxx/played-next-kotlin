package com.piticlistudio.playednext.ui.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.piticlistudio.playednext.MvpStarterApplication
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.repository.datasource.net.GameServiceFactory
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import rx.subjects.BehaviorSubject
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
    internal fun provideConnectivity(app: Application): BehaviorSubject<Connectivity> {
        return (app as MvpStarterApplication).connectivity
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

    @Provides
    @Singleton
    fun providePicasso(ctx: Context): Picasso {
        val picasso = Picasso.with(ctx)
        picasso.setIndicatorsEnabled(true)
        return picasso
    }

}
