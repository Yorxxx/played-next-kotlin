package com.piticlistudio.playednext.data.repository.datasource.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.room.CollectionDao
import com.piticlistudio.playednext.data.entity.room.GameCollectionDao
import io.reactivex.Single

@Dao
interface CollectionDaoService {

    @Query("select * from collection where id = :id")
    fun find(id: Long): Single<CollectionDao>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: CollectionDao): Long

    @Query("select collection.* from collection " +
            "LEFT JOIN game_collection ON collection.id = game_collection.collectionId " +
            "WHERE game_collection.gameId = :id")
    fun findForGame(id: Int): Single<CollectionDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameCollection(data: GameCollectionDao): Long
}