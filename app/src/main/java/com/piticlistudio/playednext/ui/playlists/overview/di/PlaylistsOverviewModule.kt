package com.piticlistudio.playednext.ui.playlists.overview.di

import android.arch.lifecycle.ViewModel
import com.piticlistudio.playednext.ui.injection.ViewModelKey
import com.piticlistudio.playednext.ui.playlists.overview.PlaylistsOverviewFragment
import com.piticlistudio.playednext.ui.playlists.overview.PlaylistsOverviewViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class PlaylistsOverviewModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistsOverviewViewModel::class)
    abstract fun bindViewModel(viewModel: PlaylistsOverviewViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeFragment(): PlaylistsOverviewFragment
}