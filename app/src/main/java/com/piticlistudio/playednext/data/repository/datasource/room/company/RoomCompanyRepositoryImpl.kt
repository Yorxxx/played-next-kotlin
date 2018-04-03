package com.piticlistudio.playednext.data.repository.datasource.room.company

import com.piticlistudio.playednext.data.entity.mapper.datasources.company.RoomCompanyMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameDeveloper
import com.piticlistudio.playednext.data.entity.room.RoomGamePublisher
import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Implementation for retrieving and storing [CompanyRoom] models from Room database
 */
class RoomCompanyRepositoryImpl @Inject constructor(private val dao: RoomCompanyService,
                                                    private val companyMapper: RoomCompanyMapper) {

    fun load(id: Int): Single<Company> {
        return dao.find(id.toLong())
                .map { companyMapper.mapFromDataLayer(it) }
    }

    fun save(data: Company): Completable {
        return Completable.defer {
            val id = dao.insert(companyMapper.mapIntoDataLayerModel(data))
            if (id != 0L) Completable.complete() else Completable.error(CompanySaveException())
        }
    }

    fun loadDevelopersForGame(id: Int): Single<List<Company>> {
        return dao.findDeveloperForGame(id)
                .map {
                    mutableListOf<Company>().apply {
                        it.forEach {
                            add(companyMapper.mapFromDataLayer(it))
                        }
                    }
                }
    }

    fun saveDeveloperForGame(id: Int, data: Company): Completable {
        return save(data)
                .andThen(Completable.defer {
                    if (dao.insertGameDeveloper(RoomGameDeveloper(id, data.id)) > 0L) {
                        Completable.complete()
                    } else {
                        Completable.error(GameDeveloperSaveException())
                    }
                })
    }

    fun loadPublishersForGame(id: Int): Single<List<Company>> {
        return dao.findPublishersForGame(id)
                .map {
                    mutableListOf<Company>().apply {
                        it.forEach {
                            add(companyMapper.mapFromDataLayer(it))
                        }
                    }
                }
    }

    fun savePublisherForGame(id: Int, data: Company): Completable {
        return save(data)
                .andThen(
                    Completable.defer {
                        if (dao.insertGamePublisher(RoomGamePublisher(id, data.id)) > 0L) {
                            Completable.complete()
                        } else {
                            Completable.error(GamePublisherSaveException())
                        }
                    }
                )
    }
}

class GamePublisherSaveException : RuntimeException()
class GameDeveloperSaveException : RuntimeException()
class CompanySaveException : RuntimeException()