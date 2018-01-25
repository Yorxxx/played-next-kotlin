package com.piticlistudio.playednext.data.repository.datasource.net

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.net.CollectionDTO
import com.piticlistudio.playednext.data.repository.datasource.CollectionDatasourceRepository
import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CollectionDTORepositoryImpl @Inject constructor(private val service: IGDBService,
                                                      private val mapper: DTOModelMapper<CollectionDTO, Collection>) : CollectionDatasourceRepository {

    override fun load(id: Int): Single<Collection> {
        return service.loadCollection(id)
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else mapper.mapFromDTO(it.get(0)) }
    }

    override fun save(data: Collection): Completable {
        return Completable.error(Throwable("Not allowed"))
    }

    override fun loadForGame(id: Int): Single<Collection> {
        return service.loadGame(id, "id,name,slug,url,created_at,updated_at,collection", "collection")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map {
                    var mapped: Collection? = null
                    it.collection?.apply {
                        mapped = mapper.mapFromDTO(it.collection!!)
                    }
                    if (mapped != null) mapped!! else throw Throwable("Retrieved data does not have collection or is invalid")
                }
                .toSingle()
    }

    override fun saveForGame(id: Int, data: Collection): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}