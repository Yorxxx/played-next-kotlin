package com.piticlistudio.playednext.data.repository.datasource.dao.image

import com.piticlistudio.playednext.data.entity.mapper.datasources.image.ImageDaoMapper
import com.piticlistudio.playednext.data.repository.datasource.GameImageDatasourceRepository
import com.piticlistudio.playednext.domain.model.GameImage
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class GameImageDaoRepositoryImpl @Inject constructor(private val dao: GameImagesDaoService,
                                                     private val mapper: ImageDaoMapper) : GameImageDatasourceRepository {

    override fun loadForGame(id: Int): Flowable<List<GameImage>> {
        return dao.findForGame(id)
                .distinctUntilChanged()
                .map {
                    val list = mutableListOf<GameImage>()
                    it.forEach { list.add(mapper.mapFromModel(it)) }
                    list
                }
    }

    override fun saveForGame(id: Int, data: GameImage): Completable {
        return Completable.defer {
            dao.insert(mapper.mapFromEntity(data).apply { gameId = id })
            Completable.complete()
        }
    }
}