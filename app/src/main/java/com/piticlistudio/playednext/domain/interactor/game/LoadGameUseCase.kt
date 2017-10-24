package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Single

class LoadGameUseCase constructor(
        private val grepository: GameRepository): SingleUseCaseWithParameter<Int, Game>{

    override fun execute(parameter: Int): Single<Game> {
        return grepository.load(parameter)
    }
}