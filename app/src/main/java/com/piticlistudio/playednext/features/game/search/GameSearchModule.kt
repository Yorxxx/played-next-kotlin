package com.piticlistudio.playednext.features.game.search

import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import dagger.Module

@Module
class GameSearchModule {

    fun providePresenter(useCase: SearchGamesUseCase): GameSearchContract.Presenter {
        return GameSearchPresenter(useCase)
    }
}