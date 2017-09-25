package com.piticlistudio.playednext.domain.interactor.game

import com.piticlistudio.playednext.domain.interactor.SingleUseCase
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Interactor for searching games
 * Created by jorge on 22/09/17.
 */
class SearchGamesUseCase @Inject constructor(private val repository: GameRepository): SingleUseCase<List<Game>> {

    override fun execute(): Single<List<Game>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}