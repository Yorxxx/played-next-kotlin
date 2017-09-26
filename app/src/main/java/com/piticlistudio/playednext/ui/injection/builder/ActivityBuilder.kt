package com.piticlistudio.playednext.ui.injection.builder

import com.piticlistudio.playednext.features.FooActivity
import com.piticlistudio.playednext.ui.injection.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun bindFooActivity(): FooActivity
}