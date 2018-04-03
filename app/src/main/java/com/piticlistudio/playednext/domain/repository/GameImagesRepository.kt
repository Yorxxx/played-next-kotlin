package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.GameImage
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Interface definition for accessing GameImages from a repository
 */
interface GameImagesRepository {

    fun loadForGame(gameId: Int): Flowable<List<GameImage>>

    fun save(gameId: Int, data: List<GameImage>): Completable
}