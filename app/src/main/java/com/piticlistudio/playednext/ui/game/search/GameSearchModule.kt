package com.piticlistudio.playednext.ui.game.search

import com.piticlistudio.playednext.features.game.search.GameSearchContract
import com.piticlistudio.playednext.features.game.search.GameSearchPresenter
import com.piticlistudio.playednext.ui.injection.PerFragment
import dagger.Module
import dagger.Provides

@Module(subcomponents = arrayOf(GameSearchComponent::class))
class GameSearchModule {

    @PerFragment
    @Provides
    fun providePresenter(presenter: GameSearchPresenter): GameSearchContract.Presenter {
        return presenter
    }
}