package com.piticlistudio.playednext.data.repository.datasource.room.franchise

import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.RoomCollectionMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameCollection
import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RoomCollectionRepositoryImpl @Inject constructor(private val dao: RoomCollectionService,
                                                       private val mapper: RoomCollectionMapper) {

    private fun save(data: Collection): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDataLayerModel(data))
            if (id != 0L) Completable.complete() else Completable.error(CollectionSaveException())
        }
    }

    fun loadForGame(id: Int): Single<Collection> {
        return dao.findForGame(id)
                .map { mapper.mapFromDataLayer(it) }
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