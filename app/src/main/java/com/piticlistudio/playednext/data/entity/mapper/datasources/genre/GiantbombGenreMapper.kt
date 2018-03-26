package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.net.GiantbombGenre
import com.piticlistudio.playednext.domain.model.Genre
import javax.inject.Inject

class GiantbombGenreMapper @Inject constructor(): DataLayerMapper<GiantbombGenre, Genre>{

    override fun mapFromDataLayer(model: GiantbombGenre): Genre {
        return Genre(model.id, model.name, model.site_detail_url)
    }
}