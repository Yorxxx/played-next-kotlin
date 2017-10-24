package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the companies. This is to be implemented by the data layer, using this
 * interface as a way of communicating
 */
interface CompanyDatasourceRepository {

    fun load(id: Int): Single<Company>

    fun save(data: Company): Completable

    fun loadDevelopersForGame(id: Int): Single<List<Company>>

    fun saveDeveloperForGame(id: Int, data: Company): Completable
}