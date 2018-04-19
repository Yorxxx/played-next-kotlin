package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Interface defining methods for the relations. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GameRelationRepository {

    fun loadWithStatus(status: GameRelationStatus): Flowable<List<GameRelation>>

    fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<GameRelation>

    fun save(data: GameRelation): Completable
}