package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

class CompanyDTOMapper @Inject constructor() : LayerDataMapper<List<CompanyDTO>?, List<Company>> {

    fun mapFromModel(type: CompanyDTO): Company {
        with(type) {
            return Company(id, name, slug, url, created_at, updated_at)
        }
    }

    override fun mapFromModel(type: List<CompanyDTO>?): List<Company> {
        val result = mutableListOf<Company>()
        type?.apply {
            type.forEach {
                result.add(mapFromModel(it))
            }
        }
        return result
    }

    override fun mapFromEntity(type: List<Company>): List<CompanyDTO>? {
        throw Throwable("Forbidden")
    }
}