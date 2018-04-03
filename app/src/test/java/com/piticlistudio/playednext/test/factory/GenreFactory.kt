package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.room.GenreDao
import com.piticlistudio.playednext.data.entity.igdb.GenreDTO
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombGenre
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomInt
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class GenreFactory {

    companion object Factory {

        fun makeGenreDao(): GenreDao {
            return GenreDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString())
        }

        fun makeGenre(name: String = randomString(), id: Int = randomInt()): Genre {
            return Genre(id, name, DataFactory.randomString())
        }

        fun makeGenreDTO(): GenreDTO {
            return GenreDTO(randomInt(), randomString(), randomString(), randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeGiantbombGenre(): GiantbombGenre = GiantbombGenre(randomInt(), randomString(), randomString())
    }
}