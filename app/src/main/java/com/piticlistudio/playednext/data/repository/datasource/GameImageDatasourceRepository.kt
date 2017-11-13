package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.GameImage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Interface defining methods for the images. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GameImageDatasourceRepository {

    fun loadForGame(id: Int): Flowable<List<GameImage>>

    fun saveForGame(id: Int, data: GameImage): Completable
}