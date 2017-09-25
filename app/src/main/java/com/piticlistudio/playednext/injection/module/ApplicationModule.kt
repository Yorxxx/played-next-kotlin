package com.piticlistudio.playednext.injection.module

import android.app.Application
import android.content.Context
import com.piticlistudio.playednext.data.remote.GameServiceFactory

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import com.piticlistudio.playednext.data.remote.MvpStarterService
import com.piticlistudio.playednext.data.remote.MvpStarterServiceFactory
import com.piticlistudio.playednext.data.game.repository.remote.GameService
import com.piticlistudio.playednext.injection.ApplicationContext

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun provideMvpStarterService(): MvpStarterService {
        return MvpStarterServiceFactory.makeStarterService()
    }

    @Provides
    @Singleton
    internal fun provideGameService(): GameService {
        return GameServiceFactory.makeGameService()
    }
}
