package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.interactor.platform.LoadPlatformsForGameUseCase
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LoadGameUseCase @Inject constructor(
        private val grepository: GameRepository,
        private val platformsForGameUseCase: LoadPlatformsForGameUseCase,
        private val imagesRepository: GameImagesRepository) : FlowableUseCaseWithParameter<Int, Game> {

    override fun execute(parameter: Int): Flowable<Game> {
        return grepository.load(parameter)
                .flatMap { loadPlatforms(it) }
                .flatMap { loadImages(it) }
    }

    private fun loadPlatforms(game: Game): Flowable<Game> {
        return platformsForGameUseCase.execute(game)
                .map {
                    game.apply { platforms = it }
                }.toFlowable()
    }

    private fun loadImages(game: Game): Flowable<Game> {
        return game.images?.let { Flowable.just(game) }
                ?: imagesRepository.loadForGame(game.id)
                .onErrorReturn { listOf() }
                .map {
                    game.apply { images = it }
                }
    }
}