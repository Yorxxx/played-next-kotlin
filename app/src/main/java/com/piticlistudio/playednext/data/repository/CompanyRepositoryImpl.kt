package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.CompanyRemoteImpl
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class CompanyRepositoryImpl constructor(private val localImpl: CompanyDaoRepositoryImpl,
                                        private val remoteImpl: CompanyRemoteImpl) : CompanyRepository {

    override fun load(id: Int): Single<Company> {
        return localImpl.load(id)
                .onErrorResumeNext {
                    remoteImpl.load(id)
                            .flatMap { localImpl.save(it).andThen(Single.just(it)) }
                }
    }

    override fun save(company: Company): Completable {
        return localImpl.save(company)
    }

    override fun loadDevelopersForGameId(id: Int): Single<List<Company>> {
        return localImpl.loadDevelopersForGame(id)
                .flatMap {
                    if (it.isEmpty()) {
                        remoteImpl.loadDevelopersForGame(id)
                                .flatMap {
                                    Observable.fromIterable(it)
                                            .flatMapSingle {
                                                localImpl.saveDeveloperForGame(id, it).andThen(Single.just(it))
                                            }.toList()
                                }
                    } else {
                        Single.just(it)
                    }
                }
    }

    override fun saveDevelopersForGame(id: Int, developers: List<Company>): Completable {
        return Observable.fromIterable(developers)
                .flatMapCompletable { localImpl.saveDeveloperForGame(id, it) }
    }

    override fun loadPublishersForGame(id: Int): Single<List<Company>> {
        return localImpl.loadPublishersForGame(id)
                .flatMap {
                    if (it.isEmpty()) {
                        remoteImpl.loadPublishersForGame(id)
                                .flatMap {
                                    Observable.fromIterable(it)
                                            .flatMapSingle {
                                                localImpl.savePublisherForGame(id, it).andThen(Single.just(it))
                                            }.toList()
                                }
                    } else {
                        Single.just(it)
                    }
                }
    }

    override fun savePublishersForGame(id: Int, publishers: List<Company>): Completable {
        return Observable.fromIterable(publishers)
                .flatMapCompletable { localImpl.savePublisherForGame(id, it) }
    }
}