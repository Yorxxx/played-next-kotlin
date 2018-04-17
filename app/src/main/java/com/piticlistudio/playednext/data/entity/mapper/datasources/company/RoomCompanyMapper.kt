package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.room.RoomCompany
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject


/**
 * Mapper for converting [RoomCompany] and [Company]
 * This mapperIGDB implements both [DataLayerMapper] and [DomainLayerMapper], which allows to map entities
 * from datalayer into domain layer, and viceversa
 */
class RoomCompanyMapper @Inject constructor() : DataLayerMapper<RoomCompany, Company>, DomainLayerMapper<Company, RoomCompany> {

    override fun mapFromDataLayer(model: RoomCompany): Company = Company(model.id, model.name, model.url)

    override fun mapIntoDataLayerModel(model: Company): RoomCompany = RoomCompany(model.id, model.name, model.url)
}