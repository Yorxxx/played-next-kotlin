package com.piticlistudio.playednext.ui.game.search.di

import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.ui.game.search.GameSearchFragment
import com.piticlistudio.playednext.ui.game.search.GameSearchViewModel
import com.piticlistudio.playednext.ui.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class GameSearchModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameSearchViewModel::class)
    abstract fun bindViewModel(viewModel: GameSearchViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeFragment(): GameSearchFragment
}