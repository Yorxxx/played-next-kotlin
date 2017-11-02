package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Platform
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the platforms. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface PlatformDatasourceRepository {

    fun load(id: Int): Single<Platform>

    fun save(data: Platform): Completable

    fun loadForGame(id: Int): Single<List<Platform>>

    fun saveForGame(id: Int, data: Platform): Completable
}