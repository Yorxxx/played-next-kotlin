package com.piticlistudio.playednext.data.repository.datasource.dao

import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GameLocalImpl @Inject constructor(private val dao: GameDaoService,
                                        private val mapper: GameDaoMapper) : GameDatasourceRepository {

    override fun load(id: Int): Single<Game> {
        return dao.findGameById(id.toLong())
                .map { mapper.mapFromEntity(it) }
    }

    override fun search(query: String): Single<List<Game>> {
        return dao.findByName(query)
                .firstElement()
                .flatMapSingle { Observable.fromIterable(it).map { mapper.mapFromEntity(it) }.toList() }
    }

    override fun save(domainModel: Game): Completable {
        return Completable.defer {
            dao.insertGame(mapper.mapFromModel(domainModel))
            Completable.complete()
        }
    }
}