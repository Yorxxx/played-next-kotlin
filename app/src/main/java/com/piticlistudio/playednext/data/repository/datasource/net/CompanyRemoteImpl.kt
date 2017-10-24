package com.piticlistudio.playednext.data.repository.datasource.net

import com.piticlistudio.playednext.data.entity.mapper.datasources.CompanyDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class CompanyRemoteImpl @Inject constructor(val service: IGDBService, val mapper: CompanyDTOMapper) : CompanyDatasourceRepository {

    override fun load(id: Int): Single<Company> {
        return service.loadCompany(id, "*")
                .map { mapper.mapFromModel(it) }
    }

    override fun save(data: Company): Completable {
        return Completable.error(Throwable("Not allowed"))
    }

    override fun loadDevelopersForGame(id: Int): Single<List<Company>> {
        return service.loadGame(id, "*")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map { mapper.mapFromModel(it.developers) }
                .toSingle()
    }

    override fun saveDeveloperForGame(id: Int, data: Company): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}