package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Interface defining methods for the games. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GameRepository {

    fun load(id: Int): Flowable<Game>

    fun search(query: String, offset: Int, limit: Int): Flowable<List<Game>>

    fun save(game: Game): Completable
}