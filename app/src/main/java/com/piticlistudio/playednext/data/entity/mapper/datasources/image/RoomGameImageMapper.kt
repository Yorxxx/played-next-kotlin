package com.piticlistudio.playednext.data.entity.mapper.datasources.image

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomGameImage
import com.piticlistudio.playednext.data.entity.room.RoomImage
import com.piticlistudio.playednext.domain.model.GameImage
import javax.inject.Inject

class RoomGameImageMapper @Inject constructor() : DataLayerMapper<RoomGameImage, GameImage>, DomainLayerMapper<GameImage, RoomGameImage> {

    override fun mapFromDataLayer(model: RoomGameImage): GameImage = GameImage(model.image.url, model.image.width, model.image.height, model.gameId)

    override fun mapIntoDataLayerModel(model: GameImage): RoomGameImage {
        val room = RoomImage(model.url, model.width, model.height)
        return RoomGameImage(room, null, model.gameId)
    }
}