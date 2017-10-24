package com.piticlistudio.playednext.data.repository.datasource.net

import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Remote implementation for retrieving Game instances. This class implements the [GameDatasourceRepository] from
 * the data layers as it is the layers responsibility for defining the operations in which data
 * store implementation can carry out.
 */
class GameRemoteImpl @Inject constructor(private val service: IGDBService,
                                         private val mapper: GameDTOMapper,
                                         private val companymapper: CompanyDTOMapper) : GameDatasourceRepository {

    override fun load(id: Int): Single<Game> {
        return service.loadGame(id, "*")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map {
                    mapper.mapFromModel(it)
                }
                .toSingle()
    }

    override fun search(query: String): Single<List<Game>> {
        return service.searchGames(0, query, "*", 20)
                .flatMap {
                    Observable.fromIterable(it)
                            .map { mapper.mapFromModel(it) }
                            .toList()
                }
    }

    override fun save(domainModel: Game): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}