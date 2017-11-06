package com.piticlistudio.playednext.data.repository.datasource.net

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Remote implementation for retrieving Game instances. This class implements the [GameDatasourceRepository] from
 * the data layers as it is the layers responsibility for defining the operations in which data
 * store implementation can carry out.
 */
class GameRemoteImpl @Inject constructor(private val service: IGDBService,
                                         private val mapper: GameDTOMapper) : GameDatasourceRepository {

    override fun load(id: Int): Flowable<Game> {
        return service.loadGame(id)
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
                .map { mapper.mapFromModel(it) }
                .toFlowable()
    }

    override fun search(query: String): Flowable<List<Game>> {
        return service.searchGames(0, query, "*", 20)
                .flatMap {
                    Observable.fromIterable(it)
                            .map { mapper.mapFromModel(it) }
                            .toList()
                }
                .toFlowable()
    }

    override fun save(domainModel: Game): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}