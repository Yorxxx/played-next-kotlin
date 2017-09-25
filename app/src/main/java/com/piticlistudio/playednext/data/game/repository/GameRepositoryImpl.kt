package com.piticlistudio.playednext.data.game.repository

import com.piticlistudio.playednext.data.game.mapper.GameEntityMapper
import com.piticlistudio.playednext.data.game.repository.remote.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.domain.repository.game.GameRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Repository implementation for the interface defined at the domain layer
 */
class GameRepositoryImpl @Inject constructor(private val remoteImpl: GameRemoteImpl,
                                             private val mapper: GameEntityMapper) : GameRepository {

    override fun load(id: Int): Single<Game> {
        return remoteImpl.load(id)
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
}