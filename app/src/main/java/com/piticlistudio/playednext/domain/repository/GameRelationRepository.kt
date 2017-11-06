package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface GameRelationRepository {

    fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<GameRelation>

    fun loadForGame(gameId: Int): Flowable<List<GameRelation>>

    fun save(data: GameRelation): Completable
}