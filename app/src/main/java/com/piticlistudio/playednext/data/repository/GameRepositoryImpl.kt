package com.piticlistudio.playednext.data.repository

import android.app.AlarmManager
import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.repository.datasource.dao.game.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation for the interface defined at the domain layer
 */
@Singleton
class GameRepositoryImpl @Inject constructor(private val remoteImpl: GameRemoteImpl,
                                             private val localImpl: GameLocalImpl) : GameRepository {

    override fun load(id: Int): Flowable<Game> {
        return localImpl.load(id)
                .onErrorResumeNext { t: Throwable ->
                    if (t is EmptyResultSetException) fetchAndCache(id).toFlowable() else throw t
                }
                .flatMap { if (shouldSyncData(it)) fetchAndCache(id).toFlowable().onErrorReturnItem(it) else Flowable.just(it) }
    }

    override fun search(query: String, offset: Int, limit: Int): Flowable<List<Game>> {
        return remoteImpl.search(query, offset, limit)
    }

    override fun save(game: Game): Completable {
        return localImpl.save(game)
    }

    private fun fetchAndCache(id: Int): Single<Game> {
        return remoteImpl.load(id)
                .firstOrError()
                .flatMap { localImpl.save(it).andThen(Single.just(it)) }
    }

    private fun shouldSyncData(data: Game): Boolean {
        return System.currentTimeMillis() - data.syncedAt > AlarmManager.INTERVAL_DAY * 10
    }
}