package com.piticlistudio.playednext.test.factory

import net.bytebuddy.utility.RandomString
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class DataFactory {

    companion object Factory {

        fun randomInt(): Int {
            return ThreadLocalRandom.current().nextInt(0, 1000 + 1)
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
    }
}