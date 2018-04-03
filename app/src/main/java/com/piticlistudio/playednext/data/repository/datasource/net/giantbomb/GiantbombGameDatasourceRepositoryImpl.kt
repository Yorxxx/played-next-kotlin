package com.piticlistudio.playednext.data.repository.datasource.net.giantbomb

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.GiantbombGameMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Remote implementation for retrieving Game instances. This class implements the [GameDatasourceRepository] from
 * the data layers as it is the layers responsibility for defining the operations in which data
 * store implementation can carry out.
 * Created by e-jegi on 3/27/2018.
 */
class GiantbombGameDatasourceRepositoryImpl @Inject constructor(private val service: GiantbombService,
                                                                private val mapper: GiantbombGameMapper) : GameDatasourceRepository {

    override fun load(id: Int): Flowable<Game> {
        return service.fetchGame(id)
                .map {
                    if (!it.error.equals("OK") || it.status_code != 1) {
                        throw GiantbombServiceException(it.status_code, it.error)
                    }
                    if (it.results == null) throw EmptyResultSetException("No results found") else it.results
                }
                .map { mapper.mapFromDataLayer(it) }
                .toFlowable()
    }

    override fun search(query: String, offset: Int, limit: Int): Flowable<List<Game>> {
        return service.searchGames(query = query, offset = offset, limit = limit)
                .map {
                    if (!it.error.equals("OK") || it.status_code != 1) {
                        throw GiantbombServiceException(it.status_code, it.error)
                    }
                    if (it.results.isEmpty()) throw EmptyResultSetException("No results found") else it.results
                }
                .map {
                    mutableListOf<Game>().apply {
                        it.forEach {
                            add(mapper.mapFromDataLayer(it))
                        }
                    }.toList()
                }.toFlowable()
    }

    override fun save(domainModel: Game): Completable = Completable.error(Throwable("Not allowed"))
}

/**
 * Thrown by this repository when an error message and or status code is returned with an error
 */
class GiantbombServiceException(val code: Int, message: String): RuntimeException(message)