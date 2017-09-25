package com.piticlistudio.playednext.injection.component

import com.piticlistudio.playednext.data.DataManager
import com.piticlistudio.playednext.data.remote.MvpStarterService
import com.piticlistudio.playednext.injection.ApplicationContext
import com.piticlistudio.playednext.injection.module.ApplicationModule
import android.app.Application
import android.content.Context
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun dataManager(): DataManager

    fun mvpBoilerplateService(): MvpStarterService
}
