package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.mapper.DTOModelMapper
import com.piticlistudio.playednext.data.entity.mapper.DaoModelMapper
import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class PlatformMapper {

    class DaoMapper @Inject constructor() : DaoModelMapper<PlatformDao, Platform> {

        override fun mapFromDao(dao: PlatformDao): Platform {
            with(dao) {
                return Platform(id, name, slug, url, created_at, updated_at)
            }
        }

        override fun mapIntoDao(entity: Platform): PlatformDao {
            with(entity) {
                return PlatformDao(id, name, slug, url, createdAt, updatedAt)
            }
        }
    }

    class DTOMapper @Inject constructor() : DTOModelMapper<PlatformDTO, Platform> {

        override fun mapFromDTO(dto: PlatformDTO): Platform {
            with(dto) {
                return Platform(id, name, slug, url, created_at, updated_at)
            }
        }
    }

}