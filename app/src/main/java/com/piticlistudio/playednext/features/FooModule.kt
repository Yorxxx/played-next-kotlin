package com.piticlistudio.playednext.features

import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.features.game.search.GameSearchContract
import com.piticlistudio.playednext.features.game.search.GameSearchPresenter
import com.piticlistudio.playednext.injection.PerActivity
import dagger.Module
import dagger.Provides

@Module
class FooModule {

    @PerActivity
    @Provides
    fun providePresenter(useCase: SearchGamesUseCase): GameSearchContract.Presenter {
        return GameSearchPresenter(useCase)
    }
}