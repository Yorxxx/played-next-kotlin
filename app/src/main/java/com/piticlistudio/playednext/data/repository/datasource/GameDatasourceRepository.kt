package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the remote layer, using this
 * interface as a way of communicating
 */
interface GameDatasourceRepository {

    fun load(id: Int): Flowable<Game>

    fun search(query: String): Flowable<List<Game>>

    fun save(domainModel: Game): Completable
}