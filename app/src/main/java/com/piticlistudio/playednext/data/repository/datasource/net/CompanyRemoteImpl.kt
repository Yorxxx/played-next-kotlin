package com.piticlistudio.playednext.data.repository.datasource.net

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.CompanyDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class CompanyRemoteImpl @Inject constructor(val service: IGDBService, val mapper: CompanyDTOMapper) : CompanyDatasourceRepository {

    override fun load(id: Int): Single<Company> {
        return service.loadCompany(id, "*")
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
                .map { mapper.mapFromModel(it) }
    }

    override fun save(data: Company): Completable {
        return Completable.error(Throwable("Not allowed"))
    }

    override fun loadDevelopersForGame(id: Int): Single<List<Company>> {
        return service.loadGame(id, "id,name,slug,url,created_at,updated_at,developers", "developers")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map { mapper.mapFromModel(it.developers) }
                .toSingle()
    }

    override fun saveDeveloperForGame(id: Int, data: Company): Completable {
        return Completable.error(Throwable("Not allowed"))
    }

    override fun loadPublishersForGame(id: Int): Single<List<Company>> {
        return service.loadGame(id, "id,name,slug,url,created_at,updated_at,publishers", "publishers")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map { mapper.mapFromModel(it.publishers) }
                .toSingle()
    }

    override fun savePublisherForGame(id: Int, data: Company): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}