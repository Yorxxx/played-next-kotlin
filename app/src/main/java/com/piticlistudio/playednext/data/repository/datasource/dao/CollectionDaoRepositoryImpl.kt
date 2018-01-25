package com.piticlistudio.playednext.data.repository.datasource.dao

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.dao.GameCollectionDao
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.repository.datasource.CollectionDatasourceRepository
import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CollectionDaoRepositoryImpl @Inject constructor(private val dao: CollectionDaoService,
                                                      private val mapper: DaoModelMapper<CollectionDao, Collection>) : CollectionDatasourceRepository {

    override fun load(id: Int): Single<Collection> {
        return dao.find(id.toLong())
                .map { mapper.mapFromDao(it) }
    }

    override fun save(data: Collection): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDao(data))
            if (id != 0L) Completable.complete() else Completable.error(Throwable("Could not save company"))
        }
    }

    override fun loadForGame(id: Int): Single<Collection> {
        return dao.findForGame(id)
                .map { mapper.mapFromDao(it) }
    }

    override fun saveForGame(id: Int, data: Collection): Completable {
        return save(data)
                .doOnComplete { dao.insertGameCollection(GameCollectionDao(id, data.id)) }
    }
}