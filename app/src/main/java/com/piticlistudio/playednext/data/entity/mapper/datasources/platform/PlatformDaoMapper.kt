package com.piticlistudio.playednext.data.entity.mapper.datasources.platform

import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.Platform
import javax.inject.Inject

class PlatformDaoMapper @Inject constructor() : LayerDataMapper<List<PlatformDao>?, List<Platform>> {

    override fun mapFromModel(type: List<PlatformDao>?): List<Platform> {
        val result = mutableListOf<Platform>()
        type?.apply {
            type.forEach {
                result.add(mapFromModel(it))
            }
        }
        return result
    }

    override fun mapFromEntity(type: List<Platform>): List<PlatformDao> {
        val result = mutableListOf<PlatformDao>()
        type.forEach {
            result.add(mapFromEntity(it))
        }
        return result
    }

    fun mapFromModel(type: PlatformDao): Platform {
        with(type) {
            return Platform(id, name, slug, url, created_at, updated_at)
        }
    }

    fun mapFromEntity(type: Platform): PlatformDao {
        with(type) {
            return PlatformDao(id, name, slug!!, url, createdAt, updatedAt)
        }
    }
}