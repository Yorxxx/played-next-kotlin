package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Single
import javax.inject.Inject

class LoadGameUseCase constructor(private val repository: GameRepository): SingleUseCaseWithParameter<Int, Game>{

    override fun execute(parameter: Int): Single<Game> {
        return repository.load(parameter)
    }
}