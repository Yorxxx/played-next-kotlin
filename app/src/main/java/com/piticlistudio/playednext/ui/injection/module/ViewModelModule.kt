package com.piticlistudio.playednext.ui.injection.module

import android.arch.lifecycle.ViewModelProvider
import com.piticlistudio.playednext.features.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}