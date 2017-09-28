package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.data.entity.GameEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the remote layer, using this
 * interface as a way of communicating
 */
interface GameDatasourceRepository {

    fun load(id: Int): Single<GameEntity>

    fun search(query: String): Single<List<GameEntity>>

    fun save(entity: GameEntity): Completable
}