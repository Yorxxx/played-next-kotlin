package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.room.RoomCompany
import com.piticlistudio.playednext.data.entity.igdb.IGDBCompany
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombCompany
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class CompanyFactory {

    companion object Factory {

        fun makeCompanyRoom(): RoomCompany {
            return RoomCompany(randomInt(), randomString(), randomString())
        }

        fun makeCompany(name: String = randomString(), id: Int = randomInt()): Company {
            return Company(id, name, randomString())
        }

        fun makeIGDBCompany(): IGDBCompany {
            return IGDBCompany(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong())
        }

        fun makeGiantbombCompany(): GiantbombCompany = GiantbombCompany(randomInt(), randomString(), randomString())
    }
}