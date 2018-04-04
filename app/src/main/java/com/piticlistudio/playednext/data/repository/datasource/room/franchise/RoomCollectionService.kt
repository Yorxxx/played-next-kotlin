package com.piticlistudio.playednext.data.repository.datasource.room.franchise

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.RoomCollection
import com.piticlistudio.playednext.data.entity.room.RoomGameCollection
import io.reactivex.Single

@Dao
interface RoomCollectionService {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: RoomCollection): Long

    @Query("select collection.* from collection " +
            "LEFT JOIN game_collection ON collection.id = game_collection.collectionId " +
            "WHERE game_collection.gameId = :id")
    fun findForGame(id: Int): Single<RoomCollection>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameCollection(data: RoomGameCollection): Long
}