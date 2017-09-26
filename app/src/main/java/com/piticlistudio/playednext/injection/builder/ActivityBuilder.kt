package com.piticlistudio.playednext.injection.builder

import com.piticlistudio.playednext.features.FooActivity
import com.piticlistudio.playednext.features.FooModule
import com.piticlistudio.playednext.injection.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(FooModule::class))
    abstract fun bindFooActivity(): FooActivity
}