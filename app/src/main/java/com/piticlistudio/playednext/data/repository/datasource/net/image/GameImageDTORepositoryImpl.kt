package com.piticlistudio.playednext.data.repository.datasource.net.image

import android.arch.persistence.room.EmptyResultSetException
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.ImageDTOMapper
import com.piticlistudio.playednext.data.repository.datasource.GameImageDatasourceRepository
import com.piticlistudio.playednext.data.repository.datasource.net.IGDBService
import com.piticlistudio.playednext.domain.model.GameImage
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GameImageDTORepositoryImpl @Inject constructor(private val service: IGDBService,
                                                     private val mapper: ImageDTOMapper) : GameImageDatasourceRepository {

    override fun loadForGame(id: Int): Flowable<List<GameImage>> {
        return service.loadGame(id, "id,name,slug,url,created_at,updated_at,screenshots", null)
                .map { if (it.isEmpty()) throw EmptyResultSetException("No results found") else it.get(0) }
                .map {
                    val result = mutableListOf<GameImage>()
                    it.screenshots?.forEach {
                        result.add(mapper.mapFromModel(it))
                    }
                    result.toList()
                }.toFlowable()
    }

    override fun saveForGame(id: Int, data: GameImage): Completable {
        return Completable.error(Throwable("Not allowed"))
    }
}