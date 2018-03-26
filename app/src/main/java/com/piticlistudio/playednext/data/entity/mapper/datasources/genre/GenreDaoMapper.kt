package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.Genre
import javax.inject.Inject

class GenreDaoMapper @Inject constructor() : LayerDataMapper<List<GenreDao>?, List<Genre>> {

    fun mapFromModel(type: GenreDao): Genre {
        with(type) {
            return Genre(id, name, slug, url, created_at, updated_at)
        }
    }

    fun mapFromEntity(type: Genre): GenreDao {
        with(type) {
            return GenreDao(id, name, slug, url, createdAt, updatedAt)
        }
    }

    override fun mapFromModel(type: List<GenreDao>?): List<Genre> {
        val result = mutableListOf<Genre>()
        type?.apply {
            type.forEach {
                result.add(mapFromModel(it))
            }
        }
        return result
    }

    override fun mapFromEntity(type: List<Genre>): List<GenreDao> {
        val result = mutableListOf<GenreDao>()
        type.forEach {
            result.add(mapFromEntity(it))
        }
        return result
    }
}