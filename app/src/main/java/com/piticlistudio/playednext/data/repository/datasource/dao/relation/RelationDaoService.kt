package com.piticlistudio.playednext.data.repository.datasource.dao.relation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.GameRelationDao
import com.piticlistudio.playednext.domain.model.GameRelation
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RelationDaoService {

    @Query("select * from game_relation where gameId = :gameId AND platformId = :platformId")
    fun findForGameAndPlatform(gameId: Int, platformId: Int): Flowable<List<GameRelationDao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: GameRelationDao): Long

    @Query("select * from game_relation where gameId = :gameId")
    fun findForGame(gameId: Int): Flowable<List<GameRelationDao>>
}