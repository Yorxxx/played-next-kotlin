package com.piticlistudio.playednext.data.repository.datasource.room.image

import com.piticlistudio.playednext.data.entity.mapper.datasources.image.RoomGameImageMapper
import com.piticlistudio.playednext.domain.model.GameImage
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RoomGameImageRepositoryImpl @Inject constructor(private val dao: RoomGameImagesService,
                                                      private val mapper: RoomGameImageMapper) {

    fun loadForGame(id: Int): Flowable<List<GameImage>> {
        return dao.findForGame(id)
                .distinctUntilChanged()
                .map {
                    mutableListOf<GameImage>().apply {
                        it.forEach {
                            add(mapper.mapFromDataLayer(it))
                        }
                    }
                }
    }

    fun saveForGame(data: GameImage): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDataLayerModel(data))
            if (id != 0L) Completable.complete() else Completable.error(GameImageSaveException())
        }
    }
}

class GameImageSaveException : RuntimeException()