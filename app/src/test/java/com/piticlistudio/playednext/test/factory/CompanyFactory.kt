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

        fun makeCompany(name: String = randomString()): Company {
            return Company(randomInt(), name, randomString(), randomString(), randomLong(),
                    randomLong())
        }

        fun makeCompanyDTO(): CompanyDTO {
            return CompanyDTO(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong())
        }
    }
}