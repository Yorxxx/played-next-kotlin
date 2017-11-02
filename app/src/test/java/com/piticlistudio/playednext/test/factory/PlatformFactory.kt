package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.domain.model.Platform

class PlatformFactory {

    companion object Factory {

        fun makePlatformDao(): PlatformDao {
            return PlatformDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makePlatform(): Platform {
            return Platform(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makePlatformDTO(): PlatformDTO {
            return PlatformDTO(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makePlatformList(size: Int = DataFactory.randomInt()): List<Platform> {
            val items: MutableList<Platform> = mutableListOf()
            repeat(size) {
                items.add(makePlatform())
            }
            return items
        }

        fun makePlatformDTOList(size: Int = DataFactory.randomInt()): List<PlatformDTO> {
            val items: MutableList<PlatformDTO> = mutableListOf()
            repeat(size) {
                items.add(makePlatformDTO())
            }
            return items
        }

        fun makePlatformDaoList(size: Int = DataFactory.randomInt()): List<PlatformDao> {
            val items: MutableList<PlatformDao> = mutableListOf()
            repeat(size) {
                items.add(makePlatformDao())
            }
            return items
        }
    }
}