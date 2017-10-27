package com.piticlistudio.playednext.ui.game.load

import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SaveGameUseCase
import com.piticlistudio.playednext.features.game.load.GameLoadContract
import com.piticlistudio.playednext.features.game.load.GameLoadPresenter
import com.piticlistudio.playednext.ui.injection.PerFragment
import dagger.Module
import dagger.Provides

@Module(subcomponents = arrayOf(GameLoadComponent::class))
class GameLoadModule {

    @PerFragment
    @Provides
    fun providePresenter(useCase: LoadGameUseCase, saveGameUseCase: SaveGameUseCase): GameLoadContract.Presenter {
        return GameLoadPresenter(useCase, saveGameUseCase)
    }
}