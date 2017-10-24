package com.piticlistudio.playednext.data.repository.datasource

import com.piticlistudio.playednext.domain.model.Company
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the games. This is to be implemented by the remote layer, using this
 * interface as a way of communicating
 */
interface CompanyDatasourceRepository {

    fun load(id: Int): Single<Company>

    fun save(data: Company): Completable
}