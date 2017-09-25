package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.SingleUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Interactor for searching games
 * Created by jorge on 22/09/17.
 */
class SearchGamesUseCase @Inject constructor(private val repository: GameRepository) : SingleUseCaseWithParameter<String, List<Game>> {

    override fun execute(parameter: String): Single<List<Game>> {
        return repository.search(parameter)
    }
}