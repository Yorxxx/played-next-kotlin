package com.piticlistudio.playednext.data.repository.datasource.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.piticlistudio.playednext.data.entity.dao.CompanyDao
import com.piticlistudio.playednext.data.entity.dao.GameDeveloperDao
import io.reactivex.Single

@Dao
interface CompanyDaoService {

    @Query("select * from company where id = :id")
    fun findCompanyById(id: Long): Single<CompanyDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompany(data: CompanyDao): Long

    @Query("select company.* from company " +
            "LEFT JOIN game_developer ON company.id = game_developer.companyId " +
            "WHERE game_developer.gameId = :id")
    fun findDeveloperForGame(id: Int): Single<List<CompanyDao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameDeveloper(data: GameDeveloperDao): Long
}