package com.piticlistudio.playednext.data.entity.room

import android.arch.persistence.room.*

@Entity(tableName = "game")
data class GameDao(@PrimaryKey val id: Int,
                   val name: String,
                   val url: String? = null,
                   @ColumnInfo(name = "created_at") val createdAt: Long,
                   @ColumnInfo(name = "updated_at") val updatedAt: Long,
                   val summary: String?,
                   val storyline: String?,
                   val collection: Int?,
                   val franchise: Int?,
                   val hypes: Int?,
                   val popularity: Double?,
                   val rating: Double?,
                   @ColumnInfo(name = "rating_count") val ratingCount: Int?,
                   @ColumnInfo(name = "aggregated_rating") val agregatedRating: Double?,
                   @ColumnInfo(name = "aggregated_rating_count") val aggregatedRatingCount: Int?,
                   @ColumnInfo(name = "total_rating") val totalRating: Double?,
                   @ColumnInfo(name = "total_rating_count") val totalRatingCount: Int?,
                   @ColumnInfo(name = "first_release_date") val firstReleaseAt: Long?,
                   @Embedded val timeToBeat: TimeToBeatDao?,
                   @Embedded val cover: CoverDao?,
                   @ColumnInfo(name = "synced_at") val syncedAt: Long)

data class TimeToBeatDao(@ColumnInfo(name = "beat_hastly") val hastly: Int?,
                         @ColumnInfo(name = "beat_normally") val normally: Int?,
                         @ColumnInfo(name = "beat_completely") val completely: Int?)

data class CoverDao(@ColumnInfo(name = "cover_url") val url: String,
                    @ColumnInfo(name = "cover_width") val width: Int?,
                    @ColumnInfo(name = "cover_height") val height: Int?)

@Entity(tableName = "company")
data class RoomCompany(@PrimaryKey val id: Int, val name: String, val url: String?)

@Entity(tableName = "genre")
data class GenreDao(@PrimaryKey val id: Int, val name: String, val url: String?)

@Entity(tableName = "collection")
data class CollectionDao(@PrimaryKey val id: Int, val name: String, val url: String?)

@Entity(tableName = "platform")
data class PlatformDao(@PrimaryKey val id: Int, val name: String, val slug: String, val url: String?, val created_at: Long, val updated_at: Long)

@Entity(tableName = "game_developer",
        primaryKeys = arrayOf("gameId", "companyId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomCompany::class, parentColumns = arrayOf("id"), childColumns = arrayOf("companyId"), onDelete = ForeignKey.CASCADE))))
data class RoomGameDeveloper(val gameId: Int, val companyId: Int)

@Entity(tableName = "game_publisher",
        primaryKeys = arrayOf("gameId", "companyId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomCompany::class, parentColumns = arrayOf("id"), childColumns = arrayOf("companyId"), onDelete = ForeignKey.CASCADE))))
data class RoomGamePublisher(val gameId: Int, val companyId: Int)

@Entity(tableName = "game_genre",
        primaryKeys = arrayOf("gameId", "genreId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = GenreDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("genreId"), onDelete = ForeignKey.CASCADE))))
data class GameGenreDao(val gameId: Int, val genreId: Int)

@Entity(tableName = "game_collection",
        primaryKeys = arrayOf("gameId", "collectionId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = CollectionDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("collectionId"), onDelete = ForeignKey.CASCADE))))
data class GameCollectionDao(val gameId: Int, val collectionId: Int)

@Entity(tableName = "game_platform",
        primaryKeys = arrayOf("gameId", "platformId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = PlatformDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("platformId"), onDelete = ForeignKey.CASCADE))))
data class GamePlatformDao(val gameId: Int, val platformId: Int)

@Entity(tableName = "game_relation",
        primaryKeys = arrayOf("gameId", "platformId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = PlatformDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("platformId"), onDelete = ForeignKey.CASCADE))
        ))
data class GameRelationDao(val gameId: Int, val platformId: Int, val status: Int, val created_at: Long, val updated_at: Long)

@Entity(tableName = "game_screenshots",
        primaryKeys = arrayOf("id"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE))
        ))
data class ScreenshotDao(val id: String, var gameId: Int?, val url: String, val width: Int?, val height: Int?)