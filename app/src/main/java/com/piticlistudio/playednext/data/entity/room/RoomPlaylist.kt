package com.piticlistudio.playednext.data.entity.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * Basic entity for Playlist.
 * Each playlist is unique by its [name]
 */
@Entity(tableName = "playlist")
data class RoomPlaylist(@PrimaryKey val name: String, val description: String?, val color: Int, val created_at: Long, val updated_at: Long)

/**
 * Defines the relation between a [RoomGame] and a [RoomPlaylist]
 * Each relation is unique by [gameId] and [playlistName], so a playlist cannot contain the same gameId twice.
 */
@Entity(tableName = "playlist_games_relation",
        primaryKeys = arrayOf("playlistName", "gameId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomPlaylist::class, parentColumns = arrayOf("name"), childColumns = arrayOf("playlistName"), onDelete = ForeignKey.CASCADE))
        ))
data class RoomPlaylistGameRelation(val playlistName: String, val gameId: Int, val created_at: Long, val updated_at: Long)