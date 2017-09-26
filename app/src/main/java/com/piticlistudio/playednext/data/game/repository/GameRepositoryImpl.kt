package com.piticlistudio.playednext.data.game.repository

import com.piticlistudio.playednext.data.game.mapper.GameEntityMapper
import com.piticlistudio.playednext.data.game.repository.local.GameLocalImpl
import com.piticlistudio.playednext.data.game.repository.remote.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Repository implementation for the interface defined at the domain layer
 */
class GameRepositoryImpl constructor(private val remoteImpl: GameRemoteImpl,
                                     private val localImpl: GameLocalImpl,
                                     private val mapper: GameEntityMapper) : GameRepository {

    override fun load(id: Int): Single<Game> {
        return localImpl.load(id)
                .onErrorResumeNext { remoteImpl.load(id) }
                .map { mapper.mapFromRemote(it) }
    }

    override fun search(query: String): Single<List<Game>> {
        return remoteImpl.search(query)
                .flatMap {
                    Observable.fromIterable(it)
                            .map { mapper.mapFromRemote(it) }
                            .toList()
                }
    }

    override fun save(game: Game): Completable {
        return localImpl.save(mapper.mapFromDomain(game))
    }
}