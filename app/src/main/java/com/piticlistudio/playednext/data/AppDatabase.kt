package com.piticlistudio.playednext.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.piticlistudio.playednext.data.cover.model.local.GameCover
import com.piticlistudio.playednext.data.cover.repository.local.CoverDao
import com.piticlistudio.playednext.data.game.model.local.LocalGame
import com.piticlistudio.playednext.data.game.repository.local.GameDao

@Database(entities = arrayOf(LocalGame::class, GameCover::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gamesDao(): GameDao
    abstract fun coversDao(): CoverDao
}