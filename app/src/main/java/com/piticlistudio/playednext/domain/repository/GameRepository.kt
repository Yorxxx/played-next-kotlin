package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GameRepository {

    fun load(id: Int): Flowable<Game>

    fun search(query: String): Flowable<List<Game>>

    fun save(game: Game): Completable
}