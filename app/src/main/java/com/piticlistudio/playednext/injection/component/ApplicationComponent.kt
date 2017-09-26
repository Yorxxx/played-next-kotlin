package com.piticlistudio.playednext.injection.component

import android.app.Application
import com.piticlistudio.playednext.MvpStarterApplication
import com.piticlistudio.playednext.injection.builder.ActivityBuilder
import com.piticlistudio.playednext.injection.module.ApplicationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, ApplicationModule::class, ActivityBuilder::class))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: MvpStarterApplication)

//    @ApplicationContext
//    fun context(): Context
//
//    fun application(): Application
//
//    fun dataManager(): DataManager
//
//    fun mvpBoilerplateService(): MvpStarterService
}
