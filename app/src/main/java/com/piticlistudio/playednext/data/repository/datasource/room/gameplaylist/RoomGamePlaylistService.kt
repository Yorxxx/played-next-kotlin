package com.piticlistudio.playednext.data.repository.datasource.room.gameplaylist

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelationEntity
import io.reactivex.Flowable

@Dao
interface RoomGamePlaylistService {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RoomPlaylistGameRelationEntity): Long

    @Query("select * from playlist_games_relation where playlistName = :playlistName")
    fun find(playlistName: String): Flowable<List<RoomPlaylistGameRelationEntity>>
}