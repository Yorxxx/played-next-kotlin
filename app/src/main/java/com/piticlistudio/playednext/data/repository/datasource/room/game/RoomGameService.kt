package com.piticlistudio.playednext.data.repository.datasource.room.game

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.room.RoomGame
import io.reactivex.Flowable

@Dao
interface RoomGameService {

    @Query("select * from game")
    fun findAll(): Flowable<List<RoomGame>>

    @Query("select * from game where id = :id")
    fun findById(id: Long): Flowable<List<RoomGame>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(game: RoomGame): Long

    @Update
    fun update(game: RoomGame): Int

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<RoomGame>>
}