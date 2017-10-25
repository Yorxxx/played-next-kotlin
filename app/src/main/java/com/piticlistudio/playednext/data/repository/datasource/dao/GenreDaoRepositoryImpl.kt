package com.piticlistudio.playednext.data.repository.datasource.dao

import com.piticlistudio.playednext.data.entity.dao.GameGenreDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.GenreDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.GenreDatasourceRepository
import com.piticlistudio.playednext.domain.model.Genre
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class GenreDaoRepositoryImpl @Inject constructor(private val dao: GenreDaoService,
                                                 private val mapper: GenreDaoMapper) : GenreDatasourceRepository {

    override fun loadForGame(id: Int): Single<List<Genre>> {
        return dao.findForGame(id)
                .map { mapper.mapFromModel(it) }
    }

    override fun insertGameGenre(id: Int, data: Genre): Completable {
        return save(data)
                .doOnComplete { dao.insertGameGenre(GameGenreDao(id, data.id)) }
    }

    override fun save(data: Genre): Completable {
        return Completable.defer {
            dao.insert(mapper.mapFromEntity(data))
            Completable.complete()
        }
    }
}