package com.piticlistudio.playednext.data.repository.datasource.room.playlist

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.room.RoomPlaylist
import io.reactivex.Flowable

@Dao
interface RoomPlaylistService {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(data: RoomPlaylist): Long

    @Update
    fun update(data: RoomPlaylist): Int

    @Delete
    fun delete(data: RoomPlaylist): Int

    @Query("select * from playlist")
    fun findAll(): Flowable<List<RoomPlaylist>>

    @Query("select * from playlist where name = :name")
    fun find(name: String): Flowable<RoomPlaylist>
}