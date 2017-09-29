package com.piticlistudio.playednext.data.repository.datasource.dao

import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GameLocalImpl @Inject constructor(private val dao: GameDaoService,
                                        private val mapper: GameDaoMapper) : GameDatasourceRepository {

    override fun load(id: Int): Single<GameDomainModel> {
        return dao.findGameById(id.toLong())
                .map { mapper.mapFromModel(it) }
    }

    override fun search(query: String): Single<List<GameDomainModel>> {
        return dao.findByName(query)
                .firstElement()
                .flatMapSingle { Observable.fromIterable(it).map { mapper.mapFromModel(it) }.toList() }
    }

    override fun save(domainModel: GameDomainModel): Completable {
        return Completable.defer {
            dao.insertGame(mapper.mapFromEntity(domainModel))
            Completable.complete()
        }
    }
}