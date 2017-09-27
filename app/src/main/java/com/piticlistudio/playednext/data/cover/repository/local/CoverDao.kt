package com.piticlistudio.playednext.data.cover.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.cover.model.local.GameCover
import io.reactivex.Single

@Dao
interface CoverDao {

    @Query("select * from cover where gameId = :id")
    fun findCoverByGameId(id: Long): Single<GameCover>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCover(cover: GameCover): Long
}