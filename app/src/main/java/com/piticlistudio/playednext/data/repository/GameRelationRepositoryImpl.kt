package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.room.relation.RelationDaoRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GameRelationRepositoryImpl @Inject constructor(private val localImpl: RelationDaoRepositoryImpl) : GameRelationRepository {

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<GameRelation> {
        return localImpl.loadForGameAndPlatform(gameId, platformId)
                .map { if (it.isEmpty()) GameRelation(null, null) else it.first() }
    }

    override fun save(data: GameRelation): Completable {
        return localImpl.save(data)
    }

    override fun loadForGame(gameId: Int): Flowable<List<GameRelation>> {
        return localImpl.loadForGame(gameId)
    }
}