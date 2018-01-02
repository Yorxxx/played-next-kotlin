package com.piticlistudio.playednext.data.repository.datasource.dao.game

import android.arch.persistence.room.*
import com.piticlistudio.playednext.data.entity.dao.*
import io.reactivex.Flowable

@Dao
interface GameDaoService {

    @Query("select * from game")
    fun findAll(): Flowable<List<GameDao>>

    @Query("select * from game where id = :id")
    fun findById(id: Long): Flowable<List<GameDao>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(game: GameDao): Long

    @Update
    fun update(game: GameDao): Int

    @Query("select * from game where name LIKE :name")
    fun findByName(name: String): Flowable<List<GameDao>>

    @Query("SELECT * FROM game AS g " +
            "LEFT JOIN (SELECT c.id AS devId, c.name as devName, c.slug AS devSlug, c.url AS devUrl, c.created_at as devCreatedAt, c.updated_at as devUpdatedAt, gd.gameId from company AS c LEFT JOIN game_developer AS gd ON c.id = gd.companyId) ON g.id = gameId " +
            "WHERE g.id = :id ")
    fun loadById(id: Long): Flowable<List<GameQueryResult>>

    class GameQueryResult(@Embedded val game: GameDao,
                          val devId: Int, val devName: String, val devSlug: String, val devUrl: String?, val devCreatedAt: Long, val devUpdatedAt: Long) {

        fun developer() = CompanyDao(devId, devName, devSlug, devUrl, devCreatedAt, devUpdatedAt)
    }
}