package com.piticlistudio.playednext.data.repository.datasource.room.image

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.RoomGameImage
import io.reactivex.Flowable

@Dao
interface RoomGameImagesService {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RoomGameImage): Long

    @Query("select * from game_screenshots where gameId = :gameId")
    fun findForGame(gameId: Int): Flowable<List<RoomGameImage>>
}