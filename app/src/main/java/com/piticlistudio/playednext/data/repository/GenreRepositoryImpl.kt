package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GenreRemoteImpl
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.domain.repository.GenreRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


class GenreRepositoryImpl constructor(private val localImpl: GenreDaoRepositoryImpl,
                                      private val remoteImpl: GenreRemoteImpl) : GenreRepository {

    override fun loadForGame(id: Int): Single<List<Genre>> {
        return localImpl.loadForGame(id)
                .flatMap {
                    if (it.isEmpty()) {
                        remoteImpl.loadForGame(id)
                                .flatMap { saveForGame(id, it).andThen(Single.just(it)) }
                    } else {
                        Single.just(it)
                    }
                }
    }

    override fun saveForGame(id: Int, genres: List<Genre>): Completable {
        return Observable.fromIterable(genres)
                .flatMapCompletable { localImpl.insertGameGenre(id, it) }
    }
}