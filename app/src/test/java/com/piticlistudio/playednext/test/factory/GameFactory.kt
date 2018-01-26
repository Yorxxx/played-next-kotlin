package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.*
import com.piticlistudio.playednext.data.entity.net.*
import com.piticlistudio.playednext.domain.model.Cover
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.TimeToBeat
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDTO
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompanyDTO
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDouble
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomIntList
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDTO
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDTO

/**
 * Factory class for Game instances
 */
class GameFactory {

    companion object Factory {

        fun makeGame(id: Int = randomInt()): Game {
            return Game(id, randomString(), randomLong(), randomLong(), randomString(),
                    randomString(), randomString(), randomDouble(), randomInt(), randomDouble(),
                    randomInt(), randomDouble(), randomInt(), randomLong(), makeCover(),
                    makeTimeToBeat(),
                    randomListOf { makeCompany() },
                    randomListOf { makeCompany() },
                    randomListOf { makeGenre() },
                    makeCollection(), randomLong(),
                    randomListOf(5) { makePlatform()},
                    randomListOf { makeGameImage() })
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
                    randomLong(), makeTimeToBeatCache(), makeCoverCache(),
                    randomLong())
        }

        fun makeGameWithRelationalData(id: Int = randomInt()): GameWithRelationalData {
            return GameWithRelationalData().apply {
                game = makeGameCache(id)
                companyIdList = randomListOf(2) { GameDeveloperDao(randomInt(), randomInt())  }
            }
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
                    randomInt(), randomDouble(), randomInt(),
                    randomListOf(factory = ::makeCompanyDTO),
                    randomListOf(factory = ::makeCompanyDTO),
                    randomIntList(), makeTimeToBeatRemote(),
                    randomListOf(factory = ::makeGenreDTO), randomLong(),
                    randomListOf(factory = this::makeReleaseDateRemote),
                    randomListOf(factory = this::makeImageRemote),
                    randomListOf(factory = this::makeVideoRemote),
                    makeImageRemote(), randomIntList(),
                    randomListOf(factory = ::makePlatformDTO))
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