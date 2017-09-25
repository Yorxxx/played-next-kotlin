package com.piticlistudio.playednext.data.game.model.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "game")
data class LocalGame(@ColumnInfo(name = "id") @PrimaryKey var id: Long = 0,
                     @ColumnInfo(name = "name") var name: String,
                     @ColumnInfo(name = "summary") var summary: String?,
                     @ColumnInfo(name = "storyline") var storyline: String?,
                     @ColumnInfo(name = "collection") var collection: Int? = 0,
                     @ColumnInfo(name = "franchise") var franchise: Int? = 0,
                     @ColumnInfo(name = "rating") var rating: Float? = 0f)