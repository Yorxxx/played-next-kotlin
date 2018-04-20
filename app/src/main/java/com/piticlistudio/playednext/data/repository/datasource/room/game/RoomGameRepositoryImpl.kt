package com.piticlistudio.playednext.data.repository.datasource.room.game

import android.arch.persistence.room.EmptyResultSetException
import android.database.sqlite.SQLiteConstraintException
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.RoomGameMapper
import com.piticlistudio.playednext.data.entity.room.RoomGame
import com.piticlistudio.playednext.data.entity.room.RoomGameProxy
import com.piticlistudio.playednext.data.repository.datasource.GameDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.room.company.RoomCompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.genre.RoomGenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.image.RoomGameImageRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.platform.RoomGamePlatformRepositoryImpl
import com.piticlistudio.playednext.domain.model.*
import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.Function6
import javax.inject.Inject

class RoomGameRepositoryImpl @Inject constructor(private val dao: RoomGameService,
                                                 private val companyRepositoryImpl: RoomCompanyRepositoryImpl,
                                                 private val imagesRepositoryImpl: RoomGameImageRepositoryImpl,
                                                 private val collectionRepositoryImpl: RoomCollectionRepositoryImpl,
                                                 private val genreRepositoryImpl: RoomGenreRepositoryImpl,
                                                 private val platformRepositoryImpl: RoomGamePlatformRepositoryImpl,
                                                 private val mapper: RoomGameMapper) : GameDatasourceRepository {

    override fun load(id: Int): Flowable<Game> {
        return loadRoomGame(id)
                .flatMap { t: RoomGame ->
                    Flowable.zip(
                            imagesRepositoryImpl.loadForGame(id),
                            companyRepositoryImpl.loadDevelopersForGame(id),
                            companyRepositoryImpl.loadPublishersForGame(id),
                            collectionRepositoryImpl.loadForGame(id),
                            genreRepositoryImpl.loadForGame(id),
                            platformRepositoryImpl.loadForGame(id),
                            Function6<List<GameImage>, List<Company>, List<Company>, List<Collection>, List<Genre>, List<Platform>, Game> { t1, t2, t3, t4, t5, t6 ->
                                mapper.mapFromDataLayer(RoomGameProxy(game = t,
                                        genres = t5,
                                        images = t1,
                                        platforms = t6,
                                        developers = t2,
                                        collection = t4.firstOrNull(),
                                        publishers = t3))
                            })
                }
    }

    private fun loadRoomGame(id: Int): Flowable<RoomGame> {
        return dao.findById(id.toLong())
                .distinctUntilChanged()
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
    }

    override fun search(query: String, offset: Int, limit: Int): Flowable<List<Game>> {
        return dao.findByName(query)
                .distinctUntilChanged()
                .map {
                    mutableListOf<Game>().apply {
                        it.forEach { add(mapper.mapFromDataLayer(RoomGameProxy(it))) }
                    }
                }
    }

    override fun save(domainModel: Game): Completable {
        return Completable.defer {
            val entity = mapper.mapIntoDataLayerModel(domainModel)
            try {
                if (dao.insert(entity.game) > 0L) {
                    Completable.complete()
                } else {
                    Completable.error(GameSaveException())
                }
            } catch (e: SQLiteConstraintException) {
                if (dao.update(entity.game) > 0) {
                    Completable.complete()
                } else {
                    Completable.error(GameUpdateException())
                }
            }

        }.andThen(Completable.defer {
            Completable.concat(mutableListOf<Completable>().apply {
                domainModel.developers.forEach {
                    add(companyRepositoryImpl.saveDeveloperForGame(domainModel.id, it))
                }
            })
        })
                .andThen(Completable.defer {
                    Completable.concat(mutableListOf<Completable>().apply {
                        domainModel.publishers.forEach {
                            add(companyRepositoryImpl.savePublisherForGame(domainModel.id, it))
                        }
                    })
                })
                .andThen(Completable.defer {
                    Completable.concat(mutableListOf<Completable>().apply {
                        domainModel.images.forEach {
                            add(imagesRepositoryImpl.saveForGame(it))
                        }
                    })
                })
                .andThen(Completable.defer {
                    Completable.concat(mutableListOf<Completable>().apply {
                        domainModel.genres.forEach {
                            add(genreRepositoryImpl.saveGenreForGame(domainModel.id, it))
                        }
                    })
                })
                .andThen(Completable.defer {
                    Completable.concat(mutableListOf<Completable>().apply {
                        domainModel.platforms.forEach {
                            add(platformRepositoryImpl.saveForGame(domainModel.id, it))
                        }
                    })
                })
                .andThen(Completable.defer {
                    if (domainModel.collection == null) {
                        Completable.complete()
                    } else {
                        collectionRepositoryImpl.saveForGame(domainModel.id, domainModel.collection!!)
                    }
                })
    }
}

class GameSaveException : RuntimeException()
class GameUpdateException : RuntimeException()