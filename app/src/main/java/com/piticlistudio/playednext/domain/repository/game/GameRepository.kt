package com.piticlistudio.playednext.domain.repository.game

import com.piticlistudio.playednext.domain.model.game.Game
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GameRepository {

    fun load(id: Int): Single<Game>

    fun search(query: String): Single<List<Game>>

    fun save(game: Game): Completable
}