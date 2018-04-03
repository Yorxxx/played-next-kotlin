package com.piticlistudio.playednext.ui.gamerelation.detail.di

import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.ui.gamerelation.detail.GameRelationDetailFragment
import com.piticlistudio.playednext.ui.gamerelation.detail.GameRelationDetailViewModel
import com.piticlistudio.playednext.ui.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class GameRelationDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameRelationDetailViewModel::class)
    abstract fun bindRelationDetailViewModel(viewModel: GameRelationDetailViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): GameRelationDetailFragment
}