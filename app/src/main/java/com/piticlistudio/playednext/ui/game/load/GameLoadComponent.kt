package com.piticlistudio.playednext.ui.game.load

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface GameLoadComponent : AndroidInjector<GameLoadFragment> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<GameLoadFragment>() { }
}