package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.data.entity.GameDomainModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the remote layer, using this
 * interface as a way of communicating
 */
interface GameDatasourceRepository {

    fun load(id: Int): Single<GameDomainModel>

    fun search(query: String): Single<List<GameDomainModel>>

    fun save(domainModel: GameDomainModel): Completable
}