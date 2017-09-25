package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import com.piticlistudio.playednext.data.game.repository.local.GameDao

@Database(entities = arrayOf(LocalGame::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): GameDao
}