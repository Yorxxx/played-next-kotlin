package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.test.factory.DataFactory.Factory.randomString

class GenreFactory {

    companion object Factory {

        fun makeGenreDao(): GenreDao {
            return GenreDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeGenre(name: String = randomString()): Genre {
            return Genre(DataFactory.randomInt(), name, DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeGenreDTO(): GenreDTO {
            return GenreDTO(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }
    }
}