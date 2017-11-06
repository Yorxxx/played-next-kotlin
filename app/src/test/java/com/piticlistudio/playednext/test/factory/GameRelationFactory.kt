package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.GameFactory.Factory.makeGame
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform

class GameRelationFactory {

    companion object Factory {

        fun makeGameRelationDao(): GameRelationDao {
            return GameRelationDao(DataFactory.randomInt(), DataFactory.randomInt(), makeRelationStatus().ordinal, randomLong(), randomLong())
        }

        fun makeGameRelation(): GameRelation {
            return GameRelation(makeGame(), makePlatform(), makeRelationStatus(), randomLong(), randomLong())
        }

        fun makeRelationStatus(): GameRelationStatus {
            return GameRelationStatus.values().get(randomInt() % GameRelationStatus.values().size)
        }
    }
}