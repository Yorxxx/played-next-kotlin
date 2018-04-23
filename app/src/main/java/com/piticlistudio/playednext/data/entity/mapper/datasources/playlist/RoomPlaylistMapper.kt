package com.piticlistudio.playednext.data.entity.mapper.datasources.playlist

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.domain.model.Playlist
import javax.inject.Inject

class RoomPlaylistMapper @Inject constructor() : DataLayerMapper<RoomPlaylistEntity, Playlist>, DomainLayerMapper<Playlist, RoomPlaylistEntity> {

    override fun mapFromDataLayer(model: RoomPlaylistEntity): Playlist = Playlist(model.name, model.description, model.color, emptyList(), model.created_at, model.updated_at)

    override fun mapIntoDataLayerModel(model: Playlist): RoomPlaylistEntity = RoomPlaylistEntity(model.name, model.description, model.color, model.createdAt, model.updatedAt)
}