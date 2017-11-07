package com.piticlistudio.playednext.ui.injection.component

import android.app.Application
import com.piticlistudio.playednext.MvpStarterApplication
import com.piticlistudio.playednext.ui.injection.builder.ActivityBuilder
import com.piticlistudio.playednext.ui.injection.builder.FragmentBuilder
import com.piticlistudio.playednext.ui.injection.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivityBuilder::class,
        FragmentBuilder::class,
        GameModule::class,
        CompanyModule::class,
        GenreModule::class,
        CollectionModule::class,
        PlatformModule::class,
        GameRelationModule::class))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: MvpStarterApplication)
}
