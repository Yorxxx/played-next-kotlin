package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Single

interface GameRelationRepository {

    fun loadForGameAndPlatform(gameId: Int, platformId: Int): Single<GameRelation>

    fun save(data: GameRelation): Completable
}