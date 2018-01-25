package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.net.CollectionDTO
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

/**
 * Mapper for collection entities
 * Created by e-jegi on 23/01/2018.
 */
class CollectionMapper {

    class DaoMapper @Inject constructor() : DaoModelMapper<CollectionDao, Collection> {

        override fun mapFromDao(dao: CollectionDao): Collection {
            with(dao) {
                return Collection(id, name, slug, url, created_at, updated_at)
            }
        }

        override fun mapIntoDao(entity: Collection): CollectionDao {
            with(entity) {
                return CollectionDao(id, name, slug, url, createdAt, updatedAt)
            }
        }
    }

    class DTOMapper @Inject constructor() : DTOModelMapper<CollectionDTO, Collection> {

        override fun mapFromDTO(dto: CollectionDTO): Collection {
            with(dto) {
                return Collection(id, name, slug, url, created_at, updated_at)
            }
        }
    }
}