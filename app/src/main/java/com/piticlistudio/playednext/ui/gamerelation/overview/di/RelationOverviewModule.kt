package com.piticlistudio.playednext.ui.gamerelation.overview.di

import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.ui.gamerelation.overview.RelationOverviewFragment
import com.piticlistudio.playednext.ui.gamerelation.overview.RelationOverviewViewModel
import com.piticlistudio.playednext.ui.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class RelationOverviewModule {

    @Binds
    @IntoMap
    @ViewModelKey(RelationOverviewViewModel::class)
    abstract fun bindViewModel(viewModel: RelationOverviewViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeFragment(): RelationOverviewFragment
}