package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.dao.GameDao
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GameDaoService {

    @Query("select * from game")
    fun getAllGames(): Flowable<List<GameDao>>

    @Query("select * from game where id = :id")
    fun findGameById(id: Long): Single<GameDao>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertGame(game: GameDao): Long

    @Update
    fun updateGame(game: GameDao): Int

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<GameDao>>
}