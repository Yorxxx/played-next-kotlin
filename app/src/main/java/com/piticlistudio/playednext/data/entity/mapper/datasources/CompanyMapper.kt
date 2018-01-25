package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.domain.model.Company
import javax.inject.Inject

class CompanyMapper @Inject constructor() {

    class DaoMapper @Inject constructor() : DaoModelMapper<CompanyDao, Company> {

        override fun mapFromDao(dao: CompanyDao): Company {
            with(dao) {
                return Company(id, name, slug, url, created_at, updated_at)
            }
        }

        override fun mapIntoDao(entity: Company): CompanyDao {
            with(entity) {
                return CompanyDao(id, name, slug, url, createdAt, updatedAt)
            }
        }
    }

    class DTOMapper @Inject constructor() : DTOModelMapper<CompanyDTO, Company> {

        override fun mapFromDTO(dto: CompanyDTO): Company {
            with(dto) {
                return Company(id, name, slug, url, created_at, updated_at)
            }
        }
    }
}