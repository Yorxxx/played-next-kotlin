package com.piticlistudio.playednext.data.game.repository.local

import com.piticlistudio.playednext.data.game.mapper.local.GameDaoMapper
import com.piticlistudio.playednext.data.game.model.GameEntity
import com.piticlistudio.playednext.data.game.repository.GameDatasourceRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class GameLocalImpl constructor(private val dao: GameDao,
                                private val mapper: GameDaoMapper) : GameDatasourceRepository {

    override fun load(id: Int): Single<GameEntity> {
        return dao.findGameById(id.toLong())
                .map { mapper.mapFromRemote(it) }
    }

    override fun search(query: String): Single<List<GameEntity>> {
        return dao.findByName(query)
                .firstElement()
                .flatMapSingle { Observable.fromIterable(it).map { mapper.mapFromRemote(it) }.toList() }
    }

    override fun save(entity: GameEntity): Completable {
        return Completable.defer {
            dao.insertGame(mapper.mapIntoDaoModel(entity))
            Completable.complete()
        }
    }
}