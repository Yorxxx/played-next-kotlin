package com.piticlistudio.playednext.data.repository

import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.CompanyRemoteImpl
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.repository.CompanyRepository
import io.reactivex.Completable
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
}