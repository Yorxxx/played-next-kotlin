package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.entity.dao.GameCache
import com.piticlistudio.playednext.data.repository.datasource.dao.GameDao

@Database(entities = arrayOf(GameCache::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): GameDao
}