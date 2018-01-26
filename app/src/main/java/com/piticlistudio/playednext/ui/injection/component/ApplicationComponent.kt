package com.piticlistudio.playednext.ui.injection.component

import android.app.Application
import com.piticlistudio.playednext.MvpStarterApplication
import com.piticlistudio.playednext.ui.injection.builder.BuildersModule
import com.piticlistudio.playednext.ui.injection.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        BuildersModule::class,
        CompanyModule::class,
        GenreModule::class,
        CollectionModule::class,
        PlatformModule::class,
        GameRelationModule::class,
        GameImagesModule::class))
interface ApplicationComponent: AndroidInjector<MvpStarterApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }
}
