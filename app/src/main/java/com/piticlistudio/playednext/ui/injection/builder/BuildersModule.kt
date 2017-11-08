package com.piticlistudio.playednext.ui.injection.builder

import android.arch.lifecycle.ViewModelProvider
import com.piticlistudio.playednext.features.FooActivity
import com.piticlistudio.playednext.features.ViewModelFactory
import com.piticlistudio.playednext.ui.gamerelation.detail.di.GameRelationDetailModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = arrayOf(GameRelationDetailModule::class))
    abstract fun bindFooActivity(): FooActivity
}