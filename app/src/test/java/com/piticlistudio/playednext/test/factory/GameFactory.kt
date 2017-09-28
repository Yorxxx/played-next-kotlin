package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.CoverEntity
import com.piticlistudio.playednext.data.entity.GameEntity
import com.piticlistudio.playednext.data.entity.TimeToBeatEntity
import com.piticlistudio.playednext.data.entity.dao.CoverCache
import com.piticlistudio.playednext.data.entity.dao.GameCache
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatCache
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

        fun makeGameCache(id: Int = randomInt()): GameCache {
            return GameCache(id, randomString(), randomString(), randomLong(), randomLong(),
                    randomString(), randomString(), randomInt(), randomInt(), randomInt(), randomDouble(),
                    randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(), randomInt(),
                    randomLong(), makeTimeToBeatCache(), makeCoverCache())
        }

        fun makeCoverCache(): CoverCache {
            return CoverCache(randomString(), randomInt(), randomInt())
        }

        fun makeTimeToBeatCache(): TimeToBeatCache {
            return TimeToBeatCache(randomInt(), randomInt(), randomInt())
        }

        fun makeGameEntity(): GameEntity {
            return GameEntity(randomInt(), randomString(), randomString(), randomLong(), randomLong(),
                    randomString(), randomString(), randomInt(), randomInt(), randomInt(), randomDouble(),
                    randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(), randomInt(),
                    randomLong(), makeTimeToBeatEntity(), makeCoverEntity())
        }

        fun makeCoverEntity(): CoverEntity {
            return CoverEntity(randomString(), randomInt(), randomInt())
        }

        fun makeTimeToBeatEntity(): TimeToBeatEntity {
            return TimeToBeatEntity(randomInt(), randomInt(), randomInt())
        }

        fun makeGameRemote(): GameRemote {
            return GameRemote(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong(), randomString(), randomString(), randomInt(), randomInt(), randomInt(),
                    randomDouble(), randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(),
                    randomInt(), randomIntList(), randomIntList(), randomIntList(), makeTimeToBeatRemote(),
                    randomIntList(), randomLong(), listOf(makeReleaseDateRemote(), makeReleaseDateRemote()),
                    listOf(makeImageRemote(), makeImageRemote(), makeImageRemote()),
                    listOf(makeVideoRemote()), makeImageRemote(), randomIntList())
        }

        fun makeTimeToBeatRemote(): TimeToBeatRemote {
            return TimeToBeatRemote(randomInt(), randomInt(), randomInt())
        }

        fun makeReleaseDateRemote(): ReleaseDateRemote {
            return ReleaseDateRemote(randomInt(), randomInt(), randomInt(), randomString())
        }

        fun makeImageRemote(): ImageRemote {
            return ImageRemote(randomString(), randomString(), randomInt(), randomInt())
        }

        fun makeVideoRemote(): VideoRemote {
            return VideoRemote(randomString(), randomString())
        }
    }
}