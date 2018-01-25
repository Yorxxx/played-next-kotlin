package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import com.piticlistudio.playednext.data.entity.dao.RelationWithGameAndPlatform
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RelationDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.PlatformDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Named

class RelationDaoRepositoryImpl @Inject constructor(private val dao: RelationDaoService,
                                                    @Named("dao") private val gamesdao: GameDatasourceRepository,
                                                    @Named("dao") private val platformDAO: PlatformDatasourceRepository,
                                                    private val mapper: DaoModelMapper<RelationWithGameAndPlatform, GameRelation>) : RelationDatasourceRepository {

    override fun save(data: GameRelation): Completable {
        assert(data.game != null)
        assert(data.platform != null)
        return gamesdao.save(data.game!!)
                .andThen(platformDAO.save(data.platform!!))
                .andThen {
                    val result = dao.insert(mapper.mapIntoDao(data).data!!)
                    if (result > 0L) it.onComplete() else it.onError(Throwable("Could not store relation"))
                }
    }

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<List<GameRelation>> {
        return dao.loadForGameAndPlatform(gameId, platformId)
                .distinctUntilChanged()
                .map {
                    val data = mutableListOf<GameRelation>()
                    it.forEach { data.add(mapper.mapFromDao(it)) }
                    data
                }
    }

    override fun loadForGame(gameId: Int): Flowable<List<GameRelation>> {
        return dao.load(gameId)
                .distinctUntilChanged()
                .map {
                    val data = mutableListOf<GameRelation>()
                    it.forEach { data.add(mapper.mapFromDao(it)) }
                    data
                }
    }
}