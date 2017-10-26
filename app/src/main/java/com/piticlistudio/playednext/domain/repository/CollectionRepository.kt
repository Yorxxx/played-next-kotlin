package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Collection
import io.reactivex.Single

/**
 * Interface defining methods for the genres. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface CollectionRepository {

    fun loadForGame(id: Int): Single<Collection>
}