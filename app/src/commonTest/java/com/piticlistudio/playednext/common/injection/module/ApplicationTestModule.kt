package com.piticlistudio.playednext.common.injection.module

import android.app.Application
import android.content.Context
import com.piticlistudio.playednext.injection.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
class ApplicationTestModule(private val mApplication: Application) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    /*************
     * MOCKS
     */

//    @Provides
//    @Singleton
//    internal fun providesDataManager(): DataManager {
//        return mock(DataManager::class.java)
//    }
//
//    @Provides
//    @Singleton
//    internal fun provideMvpBoilerplateService(): MvpStarterService {
//        return mock(MvpStarterService::class.java)
//    }

}
