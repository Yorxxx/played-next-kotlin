package com.piticlistudio.playednext.data.repository.datasource.room.genre

import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.RoomGenreMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameGenre
import com.piticlistudio.playednext.domain.model.Genre
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RoomGenreRepositoryImpl @Inject constructor(private val dao: RoomGenreService,
                                                  private val mapper: RoomGenreMapper) {

    fun loadForGame(id: Int): Single<List<Genre>> {
        return dao.findForGame(id)
                .map {
                    mutableListOf<Genre>().apply {
                        it.forEach {
                            add(mapper.mapFromDataLayer(it))
                        }
                    }
                }
    }

    /**
     * Saves the genre [data] associated with the game with identifier [id]
     */
    fun saveGenreForGame(id: Int, data: Genre): Completable {
        return save(data)
                .andThen(
                        Completable.defer {
                            if (dao.insertGameGenre(RoomGameGenre(id, data.id)) > 0L) {
                                Completable.complete()
                            } else {
                                Completable.error(GameGenreSaveException())
                            }
                        }
                )
    }

    private fun save(data: Genre): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDataLayerModel(data))
            if (id != 0L) Completable.complete() else Completable.error(GenreSaveException())
        }
    }
}

class GameGenreSaveException : RuntimeException()
class GenreSaveException : RuntimeException()
