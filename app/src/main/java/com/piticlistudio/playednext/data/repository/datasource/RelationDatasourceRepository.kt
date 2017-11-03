package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the relations. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface RelationDatasourceRepository {

    fun save(data: GameRelation): Completable

    fun loadForGameAndPlatform(gameId: Int, platformId: Int): Single<GameRelation>
}