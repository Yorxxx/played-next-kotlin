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

        fun makeGenreList(size: Int = DataFactory.randomInt()): List<Genre> {
            val items: MutableList<Genre> = mutableListOf()
            repeat(size) {
                items.add(makeGenre())
            }
            return items
        }

        fun makeGenreDTOList(size: Int = DataFactory.randomInt()): List<GenreDTO> {
            val items: MutableList<GenreDTO> = mutableListOf()
            repeat(size) {
                items.add(makeGenreDTO())
            }
            return items
        }

        fun makeGenreDAOList(size: Int = DataFactory.randomInt()): List<GenreDao> {
            val items: MutableList<GenreDao> = mutableListOf()
            repeat(size) {
                items.add(makeGenreDao())
            }
            return items
        }
    }
}