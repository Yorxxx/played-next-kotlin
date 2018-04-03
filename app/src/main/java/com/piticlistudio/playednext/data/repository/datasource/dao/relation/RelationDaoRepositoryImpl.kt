package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RelationDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RelationDaoRepositoryImpl @Inject constructor(private val dao: RelationDaoService,
                                                    private val mapper: RelationDaoMapper) : RelationDatasourceRepository {

    override fun save(data: GameRelation): Completable {
        return Completable.defer {
            val result = dao.insert(mapper.mapFromEntity(data))
            if (result > 0L) Completable.complete() else Completable.error(Throwable("Could not store relation"))
        }
    }

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<List<GameRelation>> {
        return dao.findForGameAndPlatform(gameId, platformId)
                .distinctUntilChanged()
                .map {
                    val data = mutableListOf<GameRelation>()
                    it.forEach { data.add(mapper.mapFromModel(it)) }
                    data
                }
    }

    override fun loadForGame(gameId: Int): Flowable<List<GameRelation>> {
        return dao.findForGame(gameId)
                .distinctUntilChanged()
                .map {
                    val data = mutableListOf<GameRelation>()
                    it.forEach { data.add(mapper.mapFromModel(it)) }
                    data
                }
    }
}