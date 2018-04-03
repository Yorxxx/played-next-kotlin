package com.piticlistudio.playednext.data.entity.mapper.datasources.franchise

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.giantbomb.GiantbombFranchise
import com.piticlistudio.playednext.domain.model.Collection
import javax.inject.Inject

class GiantbombCollectionMapper @Inject constructor() : DataLayerMapper<GiantbombFranchise, Collection> {

    override fun mapFromDataLayer(model: GiantbombFranchise): Collection = Collection(model.id, model.name, model.site_detail_url)
}