package com.piticlistudio.playednext.data.entity.mapper.datasources.genre

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomCompany
import com.piticlistudio.playednext.data.entity.room.RoomGenre
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.model.Genre
import javax.inject.Inject

/**
 * Mapper for converting [RoomGenre] and [Genre]
 * This mapperIGDB implements both [DataLayerMapper] and [DomainLayerMapper], which allows to map entities
 * from datalayer into domain layer, and viceversa
 */
class RoomGenreMapper @Inject constructor() : DataLayerMapper<RoomGenre, Genre>, DomainLayerMapper<Genre, RoomGenre> {

    override fun mapFromDataLayer(model: RoomGenre): Genre = Genre(model.id, model.name, model.url)

    override fun mapIntoDataLayerModel(model: Genre): RoomGenre = RoomGenre(model.id, model.name, model.url)
}