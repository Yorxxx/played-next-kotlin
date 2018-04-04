package com.piticlistudio.playednext.data.repository.datasource.room.genre

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.RoomGameGenre
import com.piticlistudio.playednext.data.entity.room.RoomGenre
import io.reactivex.Single

@Dao
interface RoomGenreService {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: RoomGenre): Long

    @Query("select genre.* from genre " +
            "LEFT JOIN game_genre ON genre.id = game_genre.genreId " +
            "WHERE game_genre.gameId = :id")
    fun findForGame(id: Int): Single<List<RoomGenre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameGenre(data: RoomGameGenre): Long
}