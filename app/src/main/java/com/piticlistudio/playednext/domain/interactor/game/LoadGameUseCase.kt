package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Single

class LoadGameUseCase constructor(
        private val grepository: GameRepository,
        private val comprepository: CompanyRepository) : SingleUseCaseWithParameter<Int, Game> {

    override fun execute(parameter: Int): Single<Game> {
        return grepository.load(parameter)
                .flatMap {
                    val game = it
                    if (it.developers == null) {
                        comprepository.loadDevelopersForGameId(parameter)
                                .onErrorReturn {
                                    listOf()
                                }
                                .map {
                                    if (!it.isEmpty()) {
                                        game.developers = it
                                    }
                                    game
                                }
                    }
                    else {
                        Single.just(game)
                    }
                }
    }
}