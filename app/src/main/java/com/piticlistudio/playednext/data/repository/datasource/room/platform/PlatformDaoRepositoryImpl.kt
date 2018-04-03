package com.piticlistudio.playednext.data.repository.datasource.room.platform

import com.piticlistudio.playednext.data.entity.room.GamePlatformDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.PlatformDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.PlatformDatasourceRepository
import com.piticlistudio.playednext.domain.model.Platform
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PlatformDaoRepositoryImpl @Inject constructor(private val dao: PlatformDaoService,
                                                    private val mapper: PlatformDaoMapper) : PlatformDatasourceRepository {

    override fun load(id: Int): Single<Platform> {
        return dao.find(id.toLong())
                .map { mapper.mapFromModel(it) }
    }

    override fun save(data: Platform): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapFromEntity(data))
            if (id != 0L) Completable.complete() else Completable.error(Throwable("Could not save platform"))
        }
    }

    override fun loadForGame(id: Int): Single<List<Platform>> {
        return dao.findForGame(id)
                .map { mapper.mapFromModel(it) }
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