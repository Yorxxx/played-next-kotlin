package com.piticlistudio.playednext.factory

import net.bytebuddy.utility.RandomString
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class DataFactory {

    companion object Factory {

        fun randomInt(bound: Int = 1000+1): Int {
            return ThreadLocalRandom.current().nextInt(1, bound)
        }

        fun randomLong(): Long {
            return randomInt().toLong()
        }

        fun randomDate(): Date {
            return Date(randomLong())
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

        fun <T> randomListOf(size: Int = randomInt(), factory: () -> T): List<T> {
            val items: MutableList<T> = mutableListOf()
            repeat(size) {
                items.add(factory())
            }
            return items
        }

        /**
         * Returns a random element.
         */
        fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null
    }
}