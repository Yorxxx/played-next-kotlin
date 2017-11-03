package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.repository.datasource.dao.relation.RelationDaoRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class GameRelationRepositoryImpl @Inject constructor(private val localImpl: RelationDaoRepositoryImpl) : GameRelationRepository {

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Single<GameRelation> {
        return localImpl.loadForGameAndPlatform(gameId, platformId)
                .onErrorResumeNext {
                    if (it is EmptyResultSetException) Single.just(GameRelation(null, null, GameRelationStatus.UNPLAYED))
                    else Single.error(it)
                }
    }

    override fun save(data: GameRelation): Completable {
        return localImpl.save(data)
    }
}