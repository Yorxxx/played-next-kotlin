package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.CollectionDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.CollectionDTORepositoryImpl
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.domain.repository.CollectionRepository
import io.reactivex.Completable
import io.reactivex.Single

class CollectionRepositoryImpl constructor(private val localImpl: CollectionDaoRepositoryImpl,
                                           private val remoteImpl: CollectionDTORepositoryImpl) : CollectionRepository {

    override fun loadForGame(id: Int): Single<Collection> {
        return localImpl.loadForGame(id)
                .onErrorResumeNext {
                    remoteImpl.loadForGame(id)
                            .flatMap { localImpl.saveForGame(id, it).andThen(Single.just(it)) }
                }
    }

    override fun saveForGame(id: Int, data: Collection): Completable {
        return localImpl.saveForGame(id, data)
    }
}