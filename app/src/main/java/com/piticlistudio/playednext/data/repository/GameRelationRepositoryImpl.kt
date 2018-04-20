package com.piticlistudio.playednext.data.repository

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.repository.GameRelationRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GameRelationRepositoryImpl @Inject constructor(private val localImpl: RelationDatasourceRepository) : GameRelationRepository {

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<GameRelation> {
        return localImpl.loadForGameAndPlatform(gameId, platformId)
                .map { if (it.isEmpty()) throw EmptyResultSetException("no items found") else it.first() }
    }

    override fun save(data: GameRelation): Completable= localImpl.save(data)

    override fun loadWithStatus(status: GameRelationStatus): Flowable<List<GameRelation>> = localImpl.loadWithStatus(status)
}