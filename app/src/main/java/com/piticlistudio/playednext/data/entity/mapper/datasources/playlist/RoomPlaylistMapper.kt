package com.piticlistudio.playednext.data.entity.mapper.datasources.playlist

import com.piticlistudio.playednext.data.entity.mapper.DataLayerMapper
import com.piticlistudio.playednext.data.entity.mapper.DomainLayerMapper
import com.piticlistudio.playednext.data.entity.room.RoomPlaylist
import com.piticlistudio.playednext.domain.model.Playlist
import javax.inject.Inject

class RoomPlaylistMapper @Inject constructor(): DataLayerMapper<RoomPlaylist, Playlist> {

    override fun mapFromDataLayer(model: RoomPlaylist): Playlist {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}