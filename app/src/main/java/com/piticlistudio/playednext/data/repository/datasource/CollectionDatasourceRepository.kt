package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the companies. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface CollectionDatasourceRepository {

    fun load(id: Int): Single<Collection>

    fun save(data: Collection): Completable

    fun loadForGame(id: Int): Single<Collection>

    fun saveForGame(id: Int, data: Collection): Completable
}