package com.piticlistudio.playednext.ui.game.search

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface GameSearchComponent: AndroidInjector<GameSearchFragment> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<GameSearchFragment>() { }
}