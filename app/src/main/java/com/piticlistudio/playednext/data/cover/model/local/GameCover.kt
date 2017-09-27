package com.piticlistudio.playednext.data.cover.model.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.piticlistudio.playednext.data.game.model.local.LocalGame

@Entity(tableName = "cover",
        foreignKeys = arrayOf(ForeignKey(entity = LocalGame::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("gameId"),
                onDelete = ForeignKey.CASCADE)))
data class GameCover(@PrimaryKey var url: String,
                     var gameId: Long,
                     var width: Int,
                     var height: Int)