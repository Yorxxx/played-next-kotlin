package com.piticlistudio.playednext.data.entity.mapper.datasources.company

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

class CompanyDaoMapper @Inject constructor() : LayerDataMapper<List<CompanyDao>?, List<Company>> {

    fun mapFromModel(type: CompanyDao): Company {
        with(type) {
            return Company(id, name, url)
        }
    }

    fun mapFromEntity(type: Company): CompanyDao {
        with(type) {
            return CompanyDao(id, name, url)
        }
    }

    override fun mapFromModel(type: List<CompanyDao>?): List<Company> {
        val result = mutableListOf<Company>()
        type?.apply {
            type.forEach {
                result.add(mapFromModel(it))
            }
        }
        return result
    }

    override fun mapFromEntity(type: List<Company>): List<CompanyDao> {
        val result = mutableListOf<CompanyDao>()
        type?.apply {
            type.forEach {
                result.add(mapFromEntity(it))
            }
        }
        return result
    }
}