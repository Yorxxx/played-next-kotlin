package com.piticlistudio.playednext.test.factory

import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.domain.model.Genre

class GenreFactory {

    companion object Factory {

        fun makeGenreDao(): GenreDao {
            return GenreDao(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeGenre(): Genre {
            return Genre(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }

        fun makeGenreDTO(): GenreDTO {
            return GenreDTO(DataFactory.randomInt(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomString(), DataFactory.randomLong(),
                    DataFactory.randomLong())
        }
    }
}