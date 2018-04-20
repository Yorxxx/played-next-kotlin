package com.piticlistudio.playednext.data.repository.datasource.room.relation

import android.util.Log
import com.piticlistudio.playednext.data.entity.mapper.datasources.relation.RoomRelationMapper
import com.piticlistudio.playednext.data.repository.datasource.RelationDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformRepositoryImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.domain.model.Platform
import com.quanturium.bouquet.annotations.RxLogger
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import javax.inject.Inject

class RoomRelationRepositoryImpl @Inject constructor(private val dao: RoomRelationService,
                                                     private val gamesRepository: RoomGameRepositoryImpl,
                                                     private val platformRepositoryImpl: RoomGamePlatformRepositoryImpl,
                                                     private val mapper: RoomRelationMapper) : RelationDatasourceRepository {

    override fun save(data: GameRelation): Completable {
        return gamesRepository.save(data.game)
                .andThen(Completable.defer {
                    val result = dao.insert(mapper.mapIntoDataLayerModel(data).relation)
                    if (result > 0L) Completable.complete() else Completable.error(RelationSaveException())
                })
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

    @RxLogger
    override fun loadWithStatus(status: GameRelationStatus): Flowable<List<GameRelation>> {
        return dao.findWithStatus(status.ordinal)
                .distinctUntilChanged()
                .flatMapSingle {
                    Flowable.fromIterable(it)
                            .flatMap {
                                return@flatMap Flowable.combineLatest(gamesRepository.load(it.gameId).firstOrError().toFlowable(),
                                        platformRepositoryImpl.load(it.platformId).firstOrError().toFlowable(),
                                        BiFunction<Game, Platform, GameRelation> { t1, t2 ->
                                            return@BiFunction GameRelation(game = t1, platform = t2, status = GameRelationStatus.values()[it.status], updatedAt = it.updated_at, createdAt = it.created_at)
                                        })
                            }
                            .toList()
                }
    }
}

class RelationSaveException : RuntimeException()