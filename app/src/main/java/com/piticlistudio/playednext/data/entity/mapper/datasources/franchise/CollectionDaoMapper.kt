package com.piticlistudio.playednext.data.entity.mapper.datasources

import com.piticlistudio.playednext.data.entity.dao.CollectionDao
import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

class CollectionDaoMapper @Inject constructor() : LayerDataMapper<CollectionDao?, Collection?> {

    override fun mapFromModel(type: CollectionDao?): Collection? {
        var collection: Collection? = null
        type?.apply {
            collection = Collection(id, name, slug, url, created_at, updated_at)
        }
        return collection
    }

    override fun mapFromEntity(type: Collection?): CollectionDao? {
        var collection: CollectionDao? = null
        type?.apply {
            collection = CollectionDao(id, name, slug, url, createdAt, updatedAt)
        }
        return collection
    }
}