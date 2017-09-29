package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.GameDao
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GameDaoService {

    @Query("select * from game")
    fun getAllGames(): Flowable<List<GameDao>>

    @Query("select * from game where id = :id")
    fun findGameById(id: Long): Single<GameDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameDao): Long

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<GameDao>>
}