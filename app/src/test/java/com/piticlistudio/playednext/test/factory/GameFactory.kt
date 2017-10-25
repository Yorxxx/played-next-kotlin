package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.CoverDao
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.entity.dao.TimeToBeatDao
import com.piticlistudio.playednext.data.entity.net.*
import com.piticlistudio.playednext.domain.model.Cover
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.TimeToBeat
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDTO
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDTOList
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyList
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDouble
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomIntList
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDTOList
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreList

/**
 * Factory class for Game instances
 */
class GameFactory {

    companion object Factory {

        fun makeGame(): Game {
            return Game(randomInt(), randomString(), randomLong(), randomLong(), randomString(),
                    randomString(), randomString(), randomDouble(), randomInt(), randomDouble(),
                    randomInt(), randomDouble(), randomInt(), randomLong(), makeCover(),
                    makeTimeToBeat(), makeCompanyList(), makeCompanyList(), makeGenreList(), makeCollection())
        }

        fun makeCover(): Cover {
            return Cover(randomString(), randomInt(), randomInt())
        }

        fun makeTimeToBeat(): TimeToBeat {
            return TimeToBeat(randomInt(), randomInt(), randomInt())
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

        fun makeGameRemote(): GameDTO {
            return GameDTO(randomInt(), randomString(), randomString(), randomString(), randomLong(),
                    randomLong(), randomString(), randomString(), makeCollectionDTO(), randomInt(),
                    randomInt(), randomDouble(), randomDouble(), randomInt(), randomDouble(),
                    randomInt(), randomDouble(), randomInt(), makeCompanyDTOList(),
                    makeCompanyDTOList(), randomIntList(), makeTimeToBeatRemote(),
                    makeGenreDTOList(), randomLong(), listOf(makeReleaseDateRemote(),
                    makeReleaseDateRemote()), listOf(makeImageRemote(), makeImageRemote(), makeImageRemote()),
                    listOf(makeVideoRemote()), makeImageRemote(), randomIntList())
        }

        fun makeEnumeratedEntityList(size: Int = randomInt()): List<BaseEnumeratedEntity> {
            val items: MutableList<BaseEnumeratedEntity> = mutableListOf()
            repeat(size) {
                items.add(makeEnumeratedEntity())
            }
            return items
        }

        fun makeEnumeratedEntity(): BaseEnumeratedEntity {
            return BaseEnumeratedEntity(randomInt(), randomString(), randomString(), randomString(),
                    randomLong(), randomLong())
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