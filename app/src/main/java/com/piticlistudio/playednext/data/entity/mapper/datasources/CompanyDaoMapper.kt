package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

class CompanyDaoMapper @Inject constructor() : LayerDataMapper<CompanyDao, Company> {

    override fun mapFromModel(type: CompanyDao): Company {
        with(type) {
            return Company(id, name, slug, url, created_at, updated_at)
        }
    }

    override fun mapFromEntity(type: Company): CompanyDao {
        with(type) {
            return CompanyDao(id, name, slug, url, createdAt, updatedAt)
        }
    }
}