package com.piticlistudio.playednext.data.repository.datasource.room.franchise

import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.RoomCollectionMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameCollection
import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Repository for retrieving [Collection] entities from Room database
 */
class RoomCollectionRepositoryImpl @Inject constructor(private val dao: RoomCollectionService,
                                                       private val mapper: RoomCollectionMapper) {

    private fun save(data: Collection): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDataLayerModel(data))
            if (id != 0L) Completable.complete() else Completable.error(CollectionSaveException())
        }
    }

    fun loadForGame(id: Int): Flowable<List<Collection>> {
        return dao.findForGame(id)
                .distinctUntilChanged()
                .map {
                    mutableListOf<Collection>().apply {
                        it.forEach { add(mapper.mapFromDataLayer(it)) }
                    }
                }
    }

    fun saveForGame(id: Int, data: Collection): Completable {
        return save(data)
                .andThen(
                        Completable.defer {
                            if (dao.insertGameCollection(RoomGameCollection(id, data.id)) > 0L) {
                                Completable.complete()
                            } else {
                                Completable.error(GameCollectionSaveException())
                            }
                        }
                )
    }
}

class CollectionSaveException : RuntimeException()
class GameCollectionSaveException : RuntimeException()