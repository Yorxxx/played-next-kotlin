package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.entity.dao.GameDao
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDaoService

@Database(entities = arrayOf(GameDao::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): GameDaoService
}