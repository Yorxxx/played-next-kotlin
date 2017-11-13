package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.image.GameImageDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.image.GameImageDTORepositoryImpl
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.repository.GameImagesRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GameImagesRepositoryImpl @Inject constructor(private val localImpl: GameImageDaoRepositoryImpl,
                                                   private val remoteImpl: GameImageDTORepositoryImpl) : GameImagesRepository {

    override fun loadForGame(gameId: Int): Flowable<List<GameImage>> {
        return localImpl.loadForGame(gameId)
                .flatMap {
                    if (!it.isEmpty())
                        Flowable.just(it)
                    else
                        fetchAndCache(gameId).toFlowable()
                }
    }

    override fun save(gameId: Int, data: List<GameImage>): Completable {
        return Observable.fromIterable(data)
                .flatMapCompletable { localImpl.saveForGame(gameId, it) }
    }

    private fun fetchAndCache(id: Int): Single<List<GameImage>> {
        return remoteImpl.loadForGame(id)
                .firstOrError()
                .flatMap {
                    Observable.fromIterable(it)
                            .flatMap { localImpl.saveForGame(id, it).andThen(Observable.just(it)) }
                            .toList()
                }

    }
}