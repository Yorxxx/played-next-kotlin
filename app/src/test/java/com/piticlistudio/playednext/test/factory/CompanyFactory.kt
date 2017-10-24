package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class CompanyFactory {

    companion object Factory {

        fun makeCompanyDao(): CompanyDao {
            return CompanyDao(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong())
        }

        fun makeCompany(): Company {
            return Company(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong())
        }

        fun makeCompanyDTO(): CompanyDTO {
            return CompanyDTO(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong())
        }

        fun makeCompanyList(size: Int = randomInt()): List<Company> {
            val items: MutableList<Company> = mutableListOf()
            repeat(size) {
                items.add(makeCompany())
            }
            return items
        }

        fun makeCompanyDTOList(size: Int = randomInt()): List<CompanyDTO> {
            val items: MutableList<CompanyDTO> = mutableListOf()
            repeat(size) {
                items.add(makeCompanyDTO())
            }
            return items
        }
    }
}