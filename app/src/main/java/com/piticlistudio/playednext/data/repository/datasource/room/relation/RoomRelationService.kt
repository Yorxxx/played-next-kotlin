package com.piticlistudio.playednext.data.repository.datasource.room.relation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.RoomGameRelation
import io.reactivex.Flowable

@Dao
interface RoomRelationService {

    @Query("select * from game_relation where gameId = :gameId AND platformId = :platformId")
    fun findForGameAndPlatform(gameId: Int, platformId: Int): Flowable<List<RoomGameRelation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RoomGameRelation): Long
}