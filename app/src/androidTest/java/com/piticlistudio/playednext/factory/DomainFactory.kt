package com.piticlistudio.playednext.factory

import com.piticlistudio.playednext.data.entity.dao.*
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import net.bytebuddy.utility.RandomString
import java.util.concurrent.ThreadLocalRandom

class DomainFactory {

    companion object Factory {

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

        fun makeGameCache(id: Int = randomInt()): GameDao {
            return GameDao(id, randomString(), randomString(), randomLong(), randomLong(),
                    randomString(), randomString(), randomInt(), randomInt(), randomInt(), randomDouble(),
                    randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(), randomInt(),
                    randomLong(), makeTimeToBeatCache(), makeCoverCache(), randomLong())
        }

        fun makeCompanyDao(id: Int = randomInt()): CompanyDao {
            return CompanyDao(id, randomString(), randomString(), randomString(), randomLong(), randomLong())
        }

        fun makeCollectionDao(id: Int = randomInt()): CollectionDao {
            return CollectionDao(id, randomString(), randomString(), randomString(), randomLong(), randomLong())
        }

        fun makeCoverCache(): CoverDao {
            return CoverDao(randomString(), randomInt(), randomInt())
        }

        fun makeTimeToBeatCache(): TimeToBeatDao {
            return TimeToBeatDao(randomInt(), randomInt(), randomInt())
        }

        fun makePlatformDao(id: Int = randomInt()): PlatformDao {
            return PlatformDao(id, randomString(), randomString(), randomString(), randomLong(), randomLong())
        }

        fun makeRelationDao(gameId: Int = randomInt(), platformId: Int = randomInt()): GameRelationDao {
            return GameRelationDao(gameId, platformId, makeRelationStatus().ordinal, randomLong(), randomLong())
        }

        fun makeRelationStatus(): GameRelationStatus {
            return GameRelationStatus.values().get(randomInt() % GameRelationStatus.values().size)
        }
    }
}