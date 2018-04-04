package com.piticlistudio.playednext.data.repository.datasource.room.platform

import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.RoomPlatformMapper
import com.piticlistudio.playednext.data.entity.room.RoomGamePlatform
import com.piticlistudio.playednext.data.repository.datasource.room.genre.GameGenreSaveException
import com.piticlistudio.playednext.domain.model.Platform
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RoomGamePlatformRepositoryImpl @Inject constructor(private val dao: RoomGamePlatformService,
                                                         private val mapper: RoomPlatformMapper) {
    private fun save(data: Platform): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDataLayerModel(data))
            if (id != 0L) Completable.complete() else Completable.error(PlatformSaveException())
        }
    }

    fun loadForGame(id: Int): Single<List<Platform>> {
        return dao.findForGame(id)
                .map {
                    mutableListOf<Platform>().apply {
                        it.forEach {
                            add(mapper.mapFromDataLayer(it))
                        }
                    }
                }
    }

    fun saveForGame(id: Int, data: Platform): Completable {
        return save(data)
                .andThen(
                        Completable.defer {
                            if (dao.insertGamePlatform(RoomGamePlatform(id, data.id)) > 0L) {
                                Completable.complete()
                            } else {
                                Completable.error(GamePlatformSaveException())
                            }
                        }
                )
    }
}

class GamePlatformSaveException : RuntimeException()
class PlatformSaveException : RuntimeException()