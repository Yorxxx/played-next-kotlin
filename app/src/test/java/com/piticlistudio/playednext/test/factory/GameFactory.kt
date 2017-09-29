package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.CoverDomainModel
import com.piticlistudio.playednext.data.entity.GameDomainModel
import com.piticlistudio.playednext.data.entity.TimeToBeatDomainModel
import com.piticlistudio.playednext.data.entity.dao.CoverDao
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatDao
import com.piticlistudio.playednext.data.entity.net.*
import com.piticlistudio.playednext.domain.model.game.Game
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDouble
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomIntList
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

/**
 * Factory class for Game instances
 */
class GameFactory {

    companion object Factory {

        fun makeGame(): Game {
            return Game(randomInt(), randomString(), randomString(), randomString())
        }

        fun makeGameCache(id: Int = randomInt()): GameDao {
            return GameDao(id, randomString(), randomString(), randomLong(), randomLong(),
                    randomString(), randomString(), randomInt(), randomInt(), randomInt(), randomDouble(),
                    randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(), randomInt(),
                    randomLong(), makeTimeToBeatCache(), makeCoverCache())
        }

        fun makeCoverCache(): CoverDao {
            return CoverDao(randomString(), randomInt(), randomInt())
        }

        fun makeTimeToBeatCache(): TimeToBeatDao {
            return TimeToBeatDao(randomInt(), randomInt(), randomInt())
        }

        fun makeGameEntity(): GameDomainModel {
            return GameDomainModel(randomInt(), randomString(), randomString(), randomLong(), randomLong(),
                    randomString(), randomString(), randomInt(), randomInt(), randomInt(), randomDouble(),
                    randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(), randomInt(),
                    randomLong(), makeTimeToBeatEntity(), makeCoverEntity())
        }

        fun makeCoverEntity(): CoverDomainModel {
            return CoverDomainModel(randomString(), randomInt(), randomInt())
        }

        fun makeTimeToBeatEntity(): TimeToBeatDomainModel {
            return TimeToBeatDomainModel(randomInt(), randomInt(), randomInt())
        }

        fun makeGameRemote(): GameDTO {
            return GameDTO(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong(), randomString(), randomString(), randomInt(), randomInt(), randomInt(),
                    randomDouble(), randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(),
                    randomInt(), randomIntList(), randomIntList(), randomIntList(), makeTimeToBeatRemote(),
                    randomIntList(), randomLong(), listOf(makeReleaseDateRemote(), makeReleaseDateRemote()),
                    listOf(makeImageRemote(), makeImageRemote(), makeImageRemote()),
                    listOf(makeVideoRemote()), makeImageRemote(), randomIntList())
        }

        fun makeTimeToBeatRemote(): TimeToBeatDTO {
            return TimeToBeatDTO(randomInt(), randomInt(), randomInt())
        }

        fun makeReleaseDateRemote(): ReleaseDateDTO {
            return ReleaseDateDTO(randomInt(), randomInt(), randomInt(), randomString())
        }

        fun makeImageRemote(): ImageDTO {
            return ImageDTO(randomString(), randomString(), randomInt(), randomInt())
        }

        fun makeVideoRemote(): VideoDTO {
            return VideoDTO(randomString(), randomString())
        }
    }
}