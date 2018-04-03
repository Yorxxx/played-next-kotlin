package com.piticlistudio.playednext.data.repository.datasource.dao.game

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.dao.GameDao
import io.reactivex.Flowable

@Dao
interface GameDaoService {

    @Query("select * from game")
    fun findAll(): Flowable<List<GameDao>>

    @Query("select * from game where id = :id")
    fun findById(id: Long): Flowable<List<GameDao>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(game: GameDao): Long

    @Update
    fun update(game: GameDao): Int

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<GameDao>>
}