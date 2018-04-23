package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistEntity
import com.piticlistudio.playednext.data.entity.room.RoomPlaylistGameRelationEntity
import io.reactivex.Flowable

@Dao
interface RoomPlaylistService {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RoomPlaylistEntity): Long

    @Delete
    fun delete(data: RoomPlaylistEntity): Int

    @Query("select * from playlist")
    fun findAll(): Flowable<List<RoomPlaylistEntity>>

    @Query("select * from playlist where name = :name")
    fun find(name: String): Flowable<List<RoomPlaylistEntity>>
}