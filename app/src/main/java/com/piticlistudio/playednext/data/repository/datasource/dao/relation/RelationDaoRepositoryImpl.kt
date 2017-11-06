package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RelationDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RelationDaoRepositoryImpl @Inject constructor(private val dao: RelationDaoService,
                                                    private val mapper: RelationDaoMapper) : RelationDatasourceRepository {

    override fun save(data: GameRelation): Completable {
        return Completable.defer {
            val result = dao.insert(mapper.mapFromEntity(data))
            if (result > 0L) Completable.complete() else Completable.error(Throwable("Could not store relation"))
        }
    }

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<GameRelation> {
        return dao.findForGameAndPlatform(gameId, platformId)
                .distinctUntilChanged()
                .map { if (it.size == 1) it.get(0) else throw EmptyResultSetException("No results found") }
                .map { mapper.mapFromModel(it) }
    }

    override fun loadForGame(gameId: Int): Flowable<List<GameRelation>> {
        return dao.findForGame(gameId)
                .distinctUntilChanged()
                .flatMap {
                    Flowable.fromIterable(it)
                            .map { mapper.mapFromModel(it) }
                }
                .toList()
                .toFlowable()

    }
}