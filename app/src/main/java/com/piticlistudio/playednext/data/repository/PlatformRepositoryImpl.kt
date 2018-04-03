package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.platform.PlatformDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.platform.PlatformDTORepositoryImpl
import com.piticlistudio.playednext.domain.model.Platform
import com.piticlistudio.playednext.domain.repository.PlatformRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlatformRepositoryImpl @Inject constructor(private val localImpl: PlatformDaoRepositoryImpl,
                                                 private val remoteImpl: PlatformDTORepositoryImpl) : PlatformRepository {

    override fun loadForGame(id: Int): Single<List<Platform>> {
        return localImpl.loadForGame(id)
                .flatMap {
                    if (it.isEmpty()) {
                        remoteImpl.loadForGame(id)
                                .flatMap { saveForGame(id, it).andThen(Single.just(it)) }
                    } else Single.just(it)
                }
    }

    override fun saveForGame(id: Int, platforms: List<Platform>): Completable {
        return Observable.fromIterable(platforms)
                .flatMapCompletable { localImpl.saveForGame(id, it) }
    }

    override fun load(id: Int): Single<Platform> {
        return localImpl.load(id)
                .onErrorResumeNext {
                    remoteImpl.load(id).flatMap { localImpl.save(it).andThen(Single.just(it)) }
                }
    }
}