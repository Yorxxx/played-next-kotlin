package com.piticlistudio.playednext.data.repository.datasource.dao

import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CompanyDaoRepositoryImpl @Inject constructor(private val dao: CompanyDaoService,
                                                   private val mapper: CompanyDaoMapper) : CompanyDatasourceRepository {

    override fun load(id: Int): Single<Company> {
        return dao.findCompanyById(id.toLong())
                .map { mapper.mapFromModel(it) }
    }

    override fun save(data: Company): Completable {
        return Completable.defer {
            dao.insertCompany(mapper.mapFromEntity(data))
            Completable.complete()
        }
    }

    override fun loadDevelopersForGame(id: Int): Single<List<Company>> {
        return dao.findDeveloperForGame(id)
                .map { mapper.mapFromModel(it) }
    }

    override fun saveDeveloperForGame(id: Int, data: Company): Completable {
        return save(data)
                .doOnComplete { dao.insertGameDeveloper(GameDeveloperDao(id, data.id)) }
    }
}