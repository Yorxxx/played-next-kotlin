package com.piticlistudio.playednext.data.repository.datasource.room.company

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.room.RoomCompany
import com.piticlistudio.playednext.data.entity.room.RoomGameDeveloper
import com.piticlistudio.playednext.data.entity.room.RoomGamePublisher
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RoomCompanyService {

    @Query("select * from company where id = :id")
    fun find(id: Long): Single<RoomCompany>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: RoomCompany): Long

    @Query("select company.* from company " +
            "LEFT JOIN game_developer ON company.id = game_developer.companyId " +
            "WHERE game_developer.gameId = :id")
    fun findDeveloperForGame(id: Int): Flowable<List<RoomCompany>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameDeveloper(data: RoomGameDeveloper): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGamePublisher(data: RoomGamePublisher): Long

    @Query("select company.* from company " +
            "LEFT JOIN game_publisher ON company.id = game_publisher.companyId " +
            "WHERE game_publisher.gameId = :id")
    fun findPublishersForGame(id: Int): Flowable<List<RoomCompany>>
}