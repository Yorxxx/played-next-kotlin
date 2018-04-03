package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.igdb.IGDBCompany
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

/**
 * Mapper for [IGDBCompany] and [Company] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into IGDB models
 */
class IGDBCompanyMapper @Inject constructor() : DataLayerMapper<IGDBCompany, Company> {

    override fun mapFromDataLayer(model: IGDBCompany): Company = Company(model.id, model.name, model.url)
}