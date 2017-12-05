package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.GameGenreDao
import com.piticlistudio.playednext.data.entity.dao.GenreDao
import io.reactivex.Single

@Dao
interface GenreDaoService {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: GenreDao): Long

    @Query("select genre.* from genre " +
            "LEFT JOIN game_genre ON genre.id = game_genre.genreId " +
            "WHERE game_genre.gameId = :id")
    fun findForGame(id: Int): Single<List<GenreDao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameGenre(data: GameGenreDao): Long
}