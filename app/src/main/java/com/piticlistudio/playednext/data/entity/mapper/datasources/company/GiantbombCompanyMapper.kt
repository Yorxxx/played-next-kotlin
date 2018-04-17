package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombCompany
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

/**
 * Mapper for [GiantbombCompany] and [Company] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into Giantbomb models
 */
class GiantbombCompanyMapper @Inject constructor() : DataLayerMapper<GiantbombCompany, Company> {

    override fun mapFromDataLayer(model: GiantbombCompany): Company = Company(model.id, model.name, model.site_detail_url)
}