package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.giantbomb.*
import com.piticlistudio.playednext.data.entity.igdb.*
import com.piticlistudio.playednext.data.entity.room.CoverDao
import com.piticlistudio.playednext.data.entity.room.GameDao
import com.piticlistudio.playednext.data.entity.room.TimeToBeatDao
import com.piticlistudio.playednext.domain.model.Cover
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.domain.model.TimeToBeat
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollectionDTO
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeIGDBCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeGiantbombCompany
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDate
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDouble
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomIntList
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGiantbombGameImage
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenreDTO
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGiantbombGenre
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeGiantbombPlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatformDTO
import java.util.*

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
                    randomListOf(5) { makePlatform() },
                    randomListOf { makeGameImage() })
        }

        fun makeGiantbombGame(developers: List<GiantbombCompany>?  = DataFactory.Factory.randomListOf(10) { makeGiantbombCompany() },
                              publishers: List<GiantbombCompany>?  = DataFactory.Factory.randomListOf(10) { makeGiantbombCompany() },
                              genres: List<GiantbombGenre>? = randomListOf(10) { makeGiantbombGenre() },
                              franchises: List<GiantbombFranchise>? = randomListOf(10) { makeGiantbombFranchise() },
                              platforms: List<GiantbombPlatform>? = randomListOf(10) { makeGiantbombPlatform() },
                              images: List<GiantbombGameImage>? = randomListOf(10) { makeGiantbombGameImage() },
                              cover: GiantbombGameImage? = makeGiantbombGameImage(),
                              original_release_date: Date? = randomDate()): GiantbombGame {
            return GiantbombGame(
                    date_added = randomDate(),
                    date_last_updated = randomDate(),
                    deck = randomString(),
                    description = randomString(),
                    id = randomInt(),
                    expected_release_day = randomInt(),
                    expected_release_month = randomInt(),
                    expected_release_quarter = randomInt(),
                    expected_release_year = randomInt(),
                    image = cover,
                    name = randomString(),
                    original_release_date = original_release_date,
                    platforms = platforms,
                    images = images,
                    publishers = publishers,
                    developers = developers,
                    franchises = franchises,
                    genres = genres,
                    site_detail_url = randomString())
        }

        fun makeGiantbombFranchise(): GiantbombFranchise = GiantbombFranchise(randomInt(), randomString())

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
                    randomListOf(factory = ::makeIGDBCompany),
                    randomListOf(factory = ::makeIGDBCompany),
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