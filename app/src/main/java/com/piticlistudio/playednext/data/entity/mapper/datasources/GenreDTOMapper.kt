package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.model.Genre
import javax.inject.Inject

class GenreDTOMapper @Inject constructor() : LayerDataMapper<List<GenreDTO>?, List<Genre>> {

    fun mapFromModel(type: GenreDTO): Genre {
        with(type) {
            return Genre(id, name, slug, url, created_at, updated_at)
        }
    }

    override fun mapFromModel(type: List<GenreDTO>?): List<Genre> {
        val result = mutableListOf<Genre>()
        type?.apply {
            type.forEach {
                result.add(mapFromModel(it))
            }
        }
        return result
    }

    override fun mapFromEntity(type: List<Genre>): List<GenreDTO> {
        throw Throwable("Forbidden")
    }
}