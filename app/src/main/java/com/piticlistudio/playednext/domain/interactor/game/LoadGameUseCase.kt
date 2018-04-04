package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Flowable
import javax.inject.Inject

class LoadGameUseCase @Inject constructor(
        private val grepository: GameRepository,
        private val platformsForGameUseCase: LoadPlatformsForGameUseCase) : FlowableUseCaseWithParameter<Int, Game> {

    override fun execute(parameter: Int): Flowable<Game> {
        return grepository.load(parameter)
                .flatMap { loadPlatforms(it) }
    }

    private fun loadPlatforms(game: Game): Flowable<Game> {
        return platformsForGameUseCase.execute(game)
                .map {
                    game.apply { platforms = it }
                }.toFlowable()
    }
}