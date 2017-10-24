package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.repository.GameRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Repository implementation for the interface defined at the domain layer
 */
class GameRepositoryImpl constructor(private val remoteImpl: GameRemoteImpl,
                                     private val localImpl: GameLocalImpl) : GameRepository {

    override fun load(id: Int): Single<Game> {
        return localImpl.load(id)
                .onErrorResumeNext {
                    remoteImpl.load(id)
                            .flatMap { localImpl.save(it).andThen(Single.just(it)) }
                }
    }

    override fun search(query: String): Single<List<Game>> {
        return remoteImpl.search(query)
    }

    override fun save(game: Game): Completable {
        return localImpl.save(game)
    }
}