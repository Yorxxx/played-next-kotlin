package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.PlatformDTO
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class PlatformDTOMapper @Inject constructor() : LayerDataMapper<List<PlatformDTO>?, List<Platform>> {

    override fun mapFromModel(type: List<PlatformDTO>?): List<Platform> {
        val result = mutableListOf<Platform>()
        type?.apply {
            forEach {
                result.add(mapFromModel(it))
            }
        }
        return result
    }

    override fun mapFromEntity(type: List<Platform>): List<PlatformDTO>? {
        throw Throwable("Forbidden")
    }

    fun mapFromModel(type: PlatformDTO): Platform {
        with(type) {
            return Platform(id, name, slug, url, created_at, updated_at)
        }
    }
}