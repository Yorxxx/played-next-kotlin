package com.piticlistudio.playednext.data.game.repository.local

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GameDao {

    @Query("select * from game")
    fun getAllGames(): Flowable<List<LocalGame>>

    @Query("select * from game where id = :id")
    fun findGameById(id: Long): Single<LocalGame>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: LocalGame)

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<LocalGame>>
//
//    @Update()
//    fun updateTask(task: LocalGame)

    @Delete
    fun deleteTask(task: LocalGame)
}