package com.piticlistudio.playednext.ui.game.load

import com.piticlistudio.playednext.features.game.load.GameLoadContract
import com.piticlistudio.playednext.features.game.load.GameLoadPresenter
import com.piticlistudio.playednext.ui.injection.PerFragment
import dagger.Module
import dagger.Provides

@Module(subcomponents = arrayOf(GameLoadComponent::class))
class GameLoadModule {

    @PerFragment
    @Provides
    fun providePresenter(presenter: GameLoadPresenter): GameLoadContract.Presenter {
        return presenter
    }
}