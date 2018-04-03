package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.data.entity.mapper.LayerDataMapper
import com.piticlistudio.playednext.data.entity.net.CollectionDTO
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

class CollectionDTOMapper @Inject constructor() : LayerDataMapper<CollectionDTO?, Collection?> {

    override fun mapFromModel(type: CollectionDTO?): Collection? {
        var collection: Collection? = null
        type?.apply {
            collection = Collection(id, name, url)
        }
        return collection
    }

    override fun mapFromEntity(type: Collection?): CollectionDTO? {
        throw Throwable("Forbidden")
    }
}