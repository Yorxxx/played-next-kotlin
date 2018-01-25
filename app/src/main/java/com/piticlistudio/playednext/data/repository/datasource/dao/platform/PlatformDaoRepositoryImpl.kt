package com.piticlistudio.playednext.data.repository.datasource.dao.platform

import com.piticlistudio.playednext.data.entity.dao.GamePlatformDao
import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.repository.datasource.PlatformDatasourceRepository
import com.piticlistudio.playednext.domain.model.Platform
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PlatformDaoRepositoryImpl @Inject constructor(private val dao: PlatformDaoService,
                                                    private val mapper: DaoModelMapper<PlatformDao, Platform>) : PlatformDatasourceRepository {

    override fun load(id: Int): Single<Platform> {
        return dao.find(id.toLong())
                .map { mapper.mapFromDao(it) }
    }

    override fun save(data: Platform): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDao(data))
            if (id != 0L) Completable.complete() else Completable.error(Throwable("Could not save platform"))
        }
    }

    override fun loadForGame(id: Int): Single<List<Platform>> {
        return dao.findForGame(id)
                .map {
                    val list = mutableListOf<Platform>()
                    it.forEach { list.add(mapper.mapFromDao(it)) }
                    list.toList()
                }
    }

    override fun saveForGame(id: Int, data: Platform): Completable {
        return save(data)
                .doOnComplete {
                    if (dao.insertGamePlatform(GamePlatformDao(id, data.id)) > 0L)
                        Completable.complete()
                    else Completable.error(Throwable("Could not save relation"))
                }
    }
}