package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.data.entity.igdb.IGDBCollection
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

/**
 * Mapper for [IGDBCollection] and [Collection] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into IGDB models
 */
class IGDBCollectionMapper @Inject constructor() : DataLayerMapper<IGDBCollection, Collection> {

    override fun mapFromDataLayer(model: IGDBCollection): Collection = Collection(model.id, model.name, model.url)
}