package com.piticlistudio.playednext.data.repository.datasource.room.platform

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.RoomGamePlatform
import com.piticlistudio.playednext.data.entity.room.RoomPlatform
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RoomGamePlatformService {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: RoomPlatform): Long

    @Query("select platform.* from platform " +
            "LEFT JOIN game_platform ON platform.id = game_platform.platformId " +
            "WHERE game_platform.gameId = :id")
    fun findForGame(id: Int): Flowable<List<RoomPlatform>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGamePlatform(data: RoomGamePlatform): Long

    @Query("select * from platform WHERE id = :id")
    fun find(id: Int): Flowable<RoomPlatform>
}