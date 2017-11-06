package com.piticlistudio.playednext.data.repository.datasource.dao.game

import android.arch.persistence.room.EmptyResultSetException
import android.database.sqlite.SQLiteConstraintException
import com.piticlistudio.playednext.data.entity.mapper.datasources.GameDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.domain.model.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class GameLocalImpl @Inject constructor(private val dao: GameDaoService,
                                        private val mapper: GameDaoMapper) : GameDatasourceRepository {

    override fun load(id: Int): Flowable<Game> {
        return dao.findById(id.toLong())
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
                .map { mapper.mapFromEntity(it) }
    }

    override fun search(query: String): Flowable<List<Game>> {
        return dao.findByName(query)
                .flatMapSingle { Observable.fromIterable(it).map { mapper.mapFromEntity(it) }.toList() }
    }

    override fun save(domainModel: Game): Completable {
        return Completable.defer {
            mapper.mapFromModel(domainModel).also {
                try {
                    dao.insert(it)
                } catch (e: SQLiteConstraintException) {
                    dao.update(it)
                }
            }
            Completable.complete()
        }
    }
}