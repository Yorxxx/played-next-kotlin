package com.piticlistudio.playednext.data.repository.datasource.net.platform

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.data.repository.datasource.PlatformDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.domain.model.Platform
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PlatformDTORepositoryImpl @Inject constructor(private val service: IGDBService,
                                                    private val mapper: DTOModelMapper<PlatformDTO, Platform>) : PlatformDatasourceRepository {

    override fun load(id: Int): Single<Platform> {
        return service.loadPlatform(id)
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
                .map { mapper.mapFromDTO(it) }
    }

    override fun save(data: Platform): Completable {
        return Completable.error(Throwable("Not allowed"))
    }

    override fun loadForGame(id: Int): Single<List<Platform>> {
        return service.loadGame(id, "id,name,slug,url,created_at,updated_at,platforms", "platforms")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map {
                    val list = mutableListOf<Platform>()
                    it.platforms?.forEach {
                        list.add(mapper.mapFromDTO(it))
                    }
                    list.toList()
                }
                .toSingle()
    }

    override fun saveForGame(id: Int, data: Platform): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}