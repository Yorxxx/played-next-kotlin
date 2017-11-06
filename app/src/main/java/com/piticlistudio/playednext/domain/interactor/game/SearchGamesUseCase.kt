package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.FlowableUseCaseWithParameter
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Interactor for searching games
 * Created by jorge on 22/09/17.
 */
class SearchGamesUseCase @Inject constructor(private val repository: GameRepository) : FlowableUseCaseWithParameter<String, List<Game>> {

    override fun execute(parameter: String): Flowable<List<Game>> {
        return repository.search(parameter)
    }
}