package com.piticlistudio.playednext.data.game.repository.local

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import io.reactivex.Completable
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
//
//    @Update()
//    fun updateTask(task: LocalGame)

    @Delete
    fun deleteTask(task: LocalGame)
}