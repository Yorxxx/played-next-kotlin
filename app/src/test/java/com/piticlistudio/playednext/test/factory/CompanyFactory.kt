package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.data.entity.net.GiantbombCompany
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class CompanyFactory {

    companion object Factory {

        fun makeCompanyDao(): CompanyDao {
            return CompanyDao(randomInt(), randomString(), randomString())
        }

        fun makeCompany(name: String = randomString(), id: Int = randomInt()): Company {
            return Company(id, name, randomString())
        }

        fun makeCompanyDTO(): CompanyDTO {
            return CompanyDTO(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong())
        }

        fun makeGiantbombCompany(): GiantbombCompany = GiantbombCompany(randomInt(), randomString(), randomString())
    }
}