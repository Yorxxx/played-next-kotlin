package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.net.GiantbombCompany
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

class GiantbombCompanyMapper @Inject constructor(): DataLayerMapper<GiantbombCompany, Company>{

    override fun mapFromDataLayer(model: GiantbombCompany): Company {
        return Company(model.id, model.name, model.site_detail_url)
    }
}