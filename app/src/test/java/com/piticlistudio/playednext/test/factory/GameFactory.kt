package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.giantbomb.*
import com.piticlistudio.playednext.data.entity.igdb.GameDTO
import com.piticlistudio.playednext.data.entity.igdb.ReleaseDateDTO
import com.piticlistudio.playednext.data.entity.igdb.VideoDTO
import com.piticlistudio.playednext.data.entity.room.GameDao
import com.piticlistudio.playednext.data.entity.room.RoomImage
import com.piticlistudio.playednext.data.entity.room.RoomTimeToBeat
import com.piticlistudio.playednext.domain.model.Game
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeGiantbombCollection
import com.piticlistudio.playednext.test.factory.CollectionFactory.Factory.makeIGDBCollection
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeGiantbombCompany
import com.piticlistudio.playednext.test.factory.CompanyFactory.Factory.makeIGDBCompany
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDate
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomDouble
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomIntList
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomListOf
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomLong
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeGiantbombGameImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeIGDBImage
import com.piticlistudio.playednext.test.factory.GameImageFactory.Factory.makeImage
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeGiantbombGenre
import com.piticlistudio.playednext.test.factory.GenreFactory.Factory.makeIGDBGenre
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeGiantbombPlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makePlatform
import com.piticlistudio.playednext.test.factory.PlatformFactory.Factory.makeIGDBPlatform
import com.piticlistudio.playednext.test.factory.TimeToBeatFactory.Factory.makeIGDBTimeToBeat
import com.piticlistudio.playednext.test.factory.TimeToBeatFactory.Factory.makeRoomTimeToBeat
import com.piticlistudio.playednext.test.factory.TimeToBeatFactory.Factory.makeTimeToBeat
import java.util.*

/**
 * Factory class for Game instances
 */
class GameFactory {

    companion object Factory {

        fun makeGame(id: Int = randomInt()): Game {
            return Game(id = id,
                    name = randomString(),
                    releasedAt = randomLong(),
                    updatedAt = randomLong(),
                    url = randomString(),
                    summary = randomString(),
                    storyline = randomString(),
                    rating = randomDouble(),
                    totalRatingCount = randomInt(),
                    aggregatedRating = randomDouble(),
                    ratingCount = randomInt(),
                    totalRating = randomDouble(),
                    aggregatedRatingCount = randomInt(),
                    createdAt = randomLong(),
                    cover = makeImage(),
                    timeToBeat = makeTimeToBeat(),
                    publishers = randomListOf { makeCompany() },
                    developers = randomListOf { makeCompany() },
                    genres = randomListOf { makeGenre() },
                    collection = makeCollection(),
                    syncedAt = randomLong(),
                    platforms = randomListOf(5) { makePlatform() },
                    images = randomListOf { makeGameImage() })
        }

        fun makeGiantbombGame(developers: List<GiantbombCompany>? = DataFactory.Factory.randomListOf(10) { makeGiantbombCompany() },
                              publishers: List<GiantbombCompany>? = DataFactory.Factory.randomListOf(10) { makeGiantbombCompany() },
                              genres: List<GiantbombGenre>? = randomListOf(10) { makeGiantbombGenre() },
                              franchises: List<GiantbombFranchise>? = randomListOf(10) { makeGiantbombCollection() },
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

        fun makeGameCache(id: Int = randomInt()): GameDao {
            return GameDao(id, randomString(), randomString(), randomLong(), randomLong(),
                    randomString(), randomString(), randomInt(), randomInt(), randomInt(), randomDouble(),
                    randomDouble(), randomInt(), randomDouble(), randomInt(), randomDouble(), randomInt(),
                    randomLong(), makeRoomTimeToBeat(), makeCoverCache(),
                    randomLong())
        }

        fun makeCoverCache(): RoomImage {
            return RoomImage(randomString(), randomInt(), randomInt())
        }

        fun makeGameRemote(): GameDTO {
            return GameDTO(id = randomInt(),
                    storyline = randomString(),
                    summary = randomString(),
                    url = randomString(),
                    created_at = randomLong(),
                    first_release_date = randomLong(),
                    name = randomString(),
                    slug = randomString(),
                    collection = makeIGDBCollection(),
                    aggregated_rating_count = randomInt(),
                    franchise = randomInt(),
                    rating = randomDouble(),
                    aggregated_rating = randomDouble(),
                    hypes = randomInt(),
                    popularity = randomDouble(),
                    rating_count = randomInt(),
                    total_rating = randomDouble(),
                    total_rating_count = randomInt(),
                    developers = randomListOf(10) { makeIGDBCompany() },
                    publishers = randomListOf(10) { makeIGDBCompany() },
                    game_engines = randomIntList(),
                    time_to_beat = makeIGDBTimeToBeat(),
                    genres = randomListOf(5) { makeIGDBGenre() },
                    updated_at = randomLong(),
                    release_dates = randomListOf(3) { makeReleaseDateRemote() },
                    screenshots = randomListOf(12) { makeIGDBImage() },
                    videos = randomListOf(2) { makeVideoRemote() },
                    cover = makeIGDBImage(),
                    games = randomIntList(),
                    platforms = randomListOf(4) { makeIGDBPlatform() })
        }

        fun makeReleaseDateRemote(): ReleaseDateDTO {
            return ReleaseDateDTO(randomInt(), randomInt(), randomInt(), randomString())
        }

        fun makeVideoRemote(): VideoDTO {
            return VideoDTO(randomString(), randomString())
        }
    }
}