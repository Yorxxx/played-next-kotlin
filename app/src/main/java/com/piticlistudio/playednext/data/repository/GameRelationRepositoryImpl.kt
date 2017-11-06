package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.repository.datasource.dao.relation.RelationDaoRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GameRelationRepositoryImpl @Inject constructor(private val localImpl: RelationDaoRepositoryImpl) : GameRelationRepository {

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<GameRelation> {
        return localImpl.loadForGameAndPlatform(gameId, platformId)
                .onErrorResumeNext { t: Throwable ->
                    if (t is EmptyResultSetException) Flowable.just(GameRelation(null, null, GameRelationStatus.NONE))
                    else Flowable.error(t)
                }

    }

    override fun save(data: GameRelation): Completable {
        return localImpl.save(data)
    }

    override fun loadForGame(gameId: Int): Flowable<List<GameRelation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}