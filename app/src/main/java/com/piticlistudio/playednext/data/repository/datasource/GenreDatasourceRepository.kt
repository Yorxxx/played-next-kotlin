package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.model.Genre
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the companies. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GenreDatasourceRepository {

    fun save(data: Genre): Completable

    fun loadForGame(id: Int): Single<List<Genre>>

    fun insertGameGenre(id: Int, data: Genre): Completable
}