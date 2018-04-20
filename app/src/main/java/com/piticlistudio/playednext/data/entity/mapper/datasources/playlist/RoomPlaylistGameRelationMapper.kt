package com.piticlistudio.playednext.data.entity.mapper.datasources.playlist

import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelation
import com.piticlistudio.playednext.domain.model.Playlist
import javax.inject.Inject

class RoomPlaylistGameRelationMapper @Inject constructor(): DomainLayerMapper<Playlist, List<RoomPlaylistGameRelation>> {

    override fun mapIntoDataLayerModel(model: Playlist): List<RoomPlaylistGameRelation> {
        return mutableListOf<RoomPlaylistGameRelation>().apply {
            model.games.forEach {
                add(RoomPlaylistGameRelation(model.name, it.id, model.createdAt, model.updatedAt))
            }
        }
    }
}