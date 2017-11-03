package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import io.reactivex.Single

@Dao
interface RelationDaoService {

    @Query("select * from game_relation where gameId = :gameId AND platformId = :platformId")
    fun findForGameAndPlatform(gameId: Int, platformId: Int): Single<GameRelationDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: GameRelationDao): Long
}