package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import com.piticlistudio.playednext.data.entity.dao.GamePublisherDao
import io.reactivex.Single

@Dao
interface CompanyDaoService {

    @Query("select * from company where id = :id")
    fun find(id: Long): Single<CompanyDao>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: CompanyDao): Long

    @Query("select company.* from company " +
            "LEFT JOIN game_developer ON company.id = game_developer.companyId " +
            "WHERE game_developer.gameId = :id")
    fun findDeveloperForGame(id: Int): Single<List<CompanyDao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameDeveloper(data: GameDeveloperDao): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGamePublisher(data: GamePublisherDao): Long

    @Query("select company.* from company " +
            "LEFT JOIN game_publisher ON company.id = game_publisher.companyId " +
            "WHERE game_publisher.gameId = :id")
    fun findPublishersForGame(id: Int): Single<List<CompanyDao>>
}