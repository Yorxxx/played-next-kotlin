package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.*
import io.reactivex.Completable
import javax.inject.Inject

class SaveGameUseCase @Inject constructor(
        private val repository: GameRepository,
        private val platformRepository: PlatformRepository) : CompletableUseCaseWithParameter<Game> {

    override fun execute(parameter: Game): Completable {
        return repository.save(parameter)
                .andThen(Completable.concat(listOf(
                        savePlatforms(parameter))))
    }

    private fun savePlatforms(game: Game): Completable {
        return game.platforms?.let {
            platformRepository.saveForGame(game.id, it)
        } ?: Completable.complete()
    }
}