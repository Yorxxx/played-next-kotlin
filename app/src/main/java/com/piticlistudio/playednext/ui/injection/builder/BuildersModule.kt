package com.piticlistudio.playednext.ui.injection.builder

import android.arch.lifecycle.ViewModelProvider
import com.piticlistudio.playednext.ui.FooActivity
import com.piticlistudio.playednext.ui.ViewModelFactory
import com.piticlistudio.playednext.ui.game.search.di.GameSearchModule
import com.piticlistudio.playednext.ui.gamerelation.detail.di.GameRelationDetailModule
import com.piticlistudio.playednext.ui.playlists.overview.di.PlaylistsOverviewModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = arrayOf(GameRelationDetailModule::class, GameSearchModule::class, PlaylistsOverviewModule::class))
    abstract fun bindFooActivity(): FooActivity
}