package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.GameCache
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GameDao {

    @Query("select * from game")
    fun getAllGames(): Flowable<List<GameCache>>

    @Query("select * from game where id = :id")
    fun findGameById(id: Long): Single<GameCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameCache): Long

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<GameCache>>
}