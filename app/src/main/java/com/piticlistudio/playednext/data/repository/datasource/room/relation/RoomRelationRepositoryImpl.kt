package com.piticlistudio.playednext.data.repository.datasource.room.relation

import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RoomRelationMapper
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformRepositoryImpl
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import javax.inject.Inject

class RoomRelationRepositoryImpl @Inject constructor(private val dao: RoomRelationService,
                                                     private val gamesRepository: RoomGameRepositoryImpl,
                                                     private val platformRepositoryImpl: RoomGamePlatformRepositoryImpl,
                                                     private val mapper: RoomRelationMapper) : RelationDatasourceRepository {

    override fun save(data: GameRelation): Completable {
        return Completable.defer {
            val result = dao.insert(mapper.mapIntoDataLayerModel(data).relation)
            if (result > 0L) Completable.complete() else Completable.error(RelationSaveException())
        }
    }

    override fun loadForGameAndPlatform(gameId: Int, platformId: Int): Flowable<List<GameRelation>> {
        return Flowable.combineLatest(gamesRepository.load(gameId), platformRepositoryImpl.load(platformId), dao.findForGameAndPlatform(gameId, platformId), Function3 { t1, t2, t3 ->
            mutableListOf<GameRelation>().apply {
                t3.forEach {
                    add(GameRelation(game = t1, platform = t2, status = GameRelationStatus.values()[it.status], updatedAt = it.updated_at, createdAt = it.created_at))
                }
            }
        })
    }
}

class RelationSaveException : RuntimeException()