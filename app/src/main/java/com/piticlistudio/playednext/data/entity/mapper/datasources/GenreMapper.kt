package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.GenreDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.net.GenreDTO
import com.piticlistudio.playednext.domain.model.Genre
import javax.inject.Inject


class GenreMapper {

    class DaoMapper @Inject constructor() : DaoModelMapper<GenreDao, Genre> {

        override fun mapFromDao(dao: GenreDao): Genre {
            with(dao) {
                return Genre(id, name, slug, url, created_at, updated_at)
            }
        }

        override fun mapIntoDao(entity: Genre): GenreDao {
            with(entity) {
                return GenreDao(id, name, slug, url, createdAt, updatedAt)
            }
        }
    }

    class DTOMapper @Inject constructor() : DTOModelMapper<GenreDTO, Genre> {

        override fun mapFromDTO(dto: GenreDTO): Genre {
            with(dto) {
                return Genre(id, name, slug, url, created_at, updated_at)
            }
        }
    }
}