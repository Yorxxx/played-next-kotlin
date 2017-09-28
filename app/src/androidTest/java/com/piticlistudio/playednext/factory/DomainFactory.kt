package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.CoverCache
import com.piticlistudio.playednext.data.entity.dao.GameCache
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatCache
import com.piticlistudio.playednext.domain.model.game.Game
import net.bytebuddy.utility.RandomString
import java.util.concurrent.ThreadLocalRandom

class DomainFactory {

    companion object Factory {

        fun makeGame(): Game {
            return Game(randomInt(), randomString(), randomString(), randomString())
        }

        fun randomInt(): Int {
            return ThreadLocalRandom.current().nextInt(0, 1000 + 1)
        }

        fun randomLong(): Long {
            return randomInt().toLong()
        }

        fun randomBoolean(): Boolean {
            return Math.random() < 0.5
        }

        fun randomDouble(): Double {
            return Math.random()
        }

        fun randomString(length: Int = 10): String {
            return RandomString(length).nextString()
        }

        fun randomIntList(count: Int = 3): List<Int> {
            val items: MutableList<Int> = mutableListOf()
            repeat(count) {
                items.add(randomInt())
            }
            return items
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
    }
}