package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Platform
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the genres. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface PlatformRepository {

    fun loadForGame(id: Int): Single<List<Platform>>

    fun saveForGame(id: Int, platforms: List<Platform>): Completable
}