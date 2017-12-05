package com.piticlistudio.playednext.data.repository.datasource.dao.platform

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.GamePlatformDao
import com.piticlistudio.playednext.data.entity.dao.PlatformDao
import io.reactivex.Single

@Dao
interface PlatformDaoService {

    @Query("select * from platform where id = :id")
    fun find(id: Long): Single<PlatformDao>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: PlatformDao): Long

    @Query("select platform.* from platform " +
            "LEFT JOIN game_platform ON platform.id = game_platform.platformId " +
            "WHERE game_platform.gameId = :id")
    fun findForGame(id: Int): Single<List<PlatformDao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGamePlatform(data: GamePlatformDao): Long
}