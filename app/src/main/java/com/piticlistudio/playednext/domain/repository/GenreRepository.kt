package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Genre
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the genres. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface GenreRepository {

    fun loadForGame(id: Int): Single<List<Genre>>

    fun saveForGame(id: Int, genres: List<Genre>): Completable
}