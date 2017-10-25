package com.piticlistudio.playednext.domain.repository

import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the companies. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface CompanyRepository {

    fun load(id: Int): Single<Company>

    fun save(company: Company): Completable

    fun loadDevelopersForGameId(id: Int): Single<List<Company>>

    fun saveDevelopersForGame(id: Int, developers: List<Company>): Completable

    fun loadPublishersForGame(id: Int): Single<List<Company>>

    fun savePublishersForGame(id: Int, publishers: List<Company>): Completable
}