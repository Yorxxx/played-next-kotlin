package com.piticlistudio.playednext.data.repository.datasource.net

import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameRemoteMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Remote implementation for retrieving Game instances. This class implements the [GameDatasourceRepository] from
 * the data layers as it is the layers responsibility for defining the operations in which data
 * store implementation can carry out.
 */
class GameRemoteImpl @Inject constructor(private val service: GameService,
                                         private val mapper: GameRemoteMapper) : GameDatasourceRepository {

    override fun load(id: Int): Single<GameEntity> {
        return service.load(id, "*")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map {
                    mapper.mapFromModel(it)
                }
                .toSingle()
    }

    override fun search(query: String): Single<List<GameEntity>> {
        return service.search(0, query, "*", 20)
                .flatMap {
                    Observable.fromIterable(it)
                            .map { mapper.mapFromModel(it) }
                            .toList()
                }
    }

    override fun save(entity: GameEntity): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}