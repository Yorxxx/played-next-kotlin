package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.CompletableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Completable

class SaveGameUseCase constructor(private val repository: GameRepository) : CompletableUseCaseWithParameter<Game> {

    override fun execute(parameter: Game): Completable {
        return repository.save(parameter)
    }
}