package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.data.entity.igdb.IGDBCompany
import com.piticlistudio.playednext.data.entity.igdb.IGDBGenre
import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.model.Genre
import javax.inject.Inject

/**
 * Mapper for [IGDBGenre] and [Genre] models.
 * This mapper implements only [DataLayerMapper], since we don't need to map domain entities into IGDB models
 */
class IGDBGenreMapper @Inject constructor() : DataLayerMapper<IGDBGenre, Genre> {

    override fun mapFromDataLayer(model: IGDBGenre): Genre = Genre(model.id, model.name, model.url)
}