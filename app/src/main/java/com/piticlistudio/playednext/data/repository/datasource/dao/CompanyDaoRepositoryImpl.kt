package com.piticlistudio.playednext.data.repository.datasource.dao

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.data.entity.dao.GamePublisherDao
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.repository.datasource.CompanyDatasourceRepository
import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CompanyDaoRepositoryImpl @Inject constructor(private val dao: CompanyDaoService,
                                                   private val mapper: DaoModelMapper<CompanyDao, Company>) : CompanyDatasourceRepository {

    override fun load(id: Int): Single<Company> {
        return dao.find(id.toLong())
                .map { mapper.mapFromDao(it) }
    }

    override fun save(data: Company): Completable {
        return Completable.defer {
            val id = dao.insert(mapper.mapIntoDao(data))
            if (id != 0L) Completable.complete() else Completable.error(Throwable("Could not save company"))
        }
    }

    override fun loadDevelopersForGame(id: Int): Single<List<Company>> {
        return dao.findDeveloperForGame(id)
                .map {
                    val list = mutableListOf<Company>()
                    it.forEach {
                        list.add(mapper.mapFromDao(it))
                    }
                    list
                }
    }

    override fun saveDeveloperForGame(id: Int, data: Company): Completable {
        return save(data)
                .doOnComplete {
                    if (dao.insertGameDeveloper(GameDeveloperDao(id, data.id)) > 0L) {
                        Completable.complete()
                    } else {
                        Completable.error(Throwable("Could not save game developer"))
                    }
                }
    }

    override fun loadPublishersForGame(id: Int): Single<List<Company>> {
        return dao.findPublishersForGame(id)
                .map {
                    val list = mutableListOf<Company>()
                    it.forEach {
                        list.add(mapper.mapFromDao(it))
                    }
                    list
                }
    }

    override fun savePublisherForGame(id: Int, data: Company): Completable {
        return save(data)
                .doOnComplete {
                    if (dao.insertGamePublisher(GamePublisherDao(id, data.id)) > 0L) {
                        Completable.complete()
                    } else {
                        Completable.error(Throwable("Could not save game developer"))
                    }
                }
    }
}