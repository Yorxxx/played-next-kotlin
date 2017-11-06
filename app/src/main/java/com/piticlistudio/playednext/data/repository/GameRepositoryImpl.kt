package com.piticlistudio.playednext.data.repository

import android.app.AlarmManager
import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation for the interface defined at the domain layer
 */
@Singleton
class GameRepositoryImpl @Inject constructor(private val remoteImpl: GameRemoteImpl,
                                             private val localImpl: GameLocalImpl) : GameRepository {

    override fun load(id: Int): Single<Game> {
        return localImpl.load(id)
                .flatMap { if (shouldSyncData(it)) fetchAndCache(id).onErrorReturnItem(it) else Single.just(it) }
                .onErrorResumeNext { fetchAndCache(id) }
    }

    override fun search(query: String): Single<List<Game>> {
        return remoteImpl.search(query)
    }

    override fun save(game: Game): Completable {
        return localImpl.save(game)
    }

    private fun fetchAndCache(id: Int): Single<Game> {
        return remoteImpl.load(id)
                .flatMap { localImpl.save(it).andThen(Single.just(it)) }
    }

    private fun shouldSyncData(data: Game): Boolean {
        return System.currentTimeMillis() - data.syncedAt > AlarmManager.INTERVAL_DAY * 10
    }
}