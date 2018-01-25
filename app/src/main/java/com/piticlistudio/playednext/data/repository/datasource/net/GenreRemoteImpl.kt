package com.piticlistudio.playednext.data.repository.datasource.net

import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.data.repository.datasource.GenreDatasourceRepository
import com.piticlistudio.playednext.domain.model.Genre
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class GenreRemoteImpl @Inject constructor(val service: IGDBService, val mapper: DTOModelMapper<GenreDTO, Genre>) : GenreDatasourceRepository {

    override fun save(data: Genre): Completable {
        return Completable.error(Throwable("Not allowed"))
    }

    override fun loadForGame(id: Int): Single<List<Genre>> {
        return service.loadGame(id, "id,name,slug,url,created_at,updated_at,genres", "genres")
                .filter { it.size == 1 }
                .map { it.get(0) }
                .map {
                    val list = mutableListOf<Genre>()
                    it.genres?.forEach {
                        list.add(mapper.mapFromDTO(it))
                    }
                    list.toList()
                }
                .toSingle()
    }

    override fun insertGameGenre(id: Int, data: Genre): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}