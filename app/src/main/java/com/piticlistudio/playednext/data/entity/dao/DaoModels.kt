package com.piticlistudio.playednext.data.entity.dao

import android.arch.persistence.room.*

@Entity(tableName = "game")
data class GameDao(@PrimaryKey val id: Int,
                   val name: String,
                   val url: String,
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
                   @Embedded val cover: CoverDao?)

data class TimeToBeatDao(@ColumnInfo(name = "beat_hastly") val hastly: Int?,
                         @ColumnInfo(name = "beat_normally") val normally: Int?,
                         @ColumnInfo(name = "beat_completely") val completely: Int?)

data class CoverDao(@ColumnInfo(name = "cover_url") val url: String,
                    @ColumnInfo(name = "cover_width") val width: Int?,
                    @ColumnInfo(name = "cover_height") val height: Int?)

@Entity(tableName = "company")
data class CompanyDao(@PrimaryKey val id: Int, val name: String, val slug: String, val url: String, val created_at: Long, val updated_at: Long)

@Entity(tableName = "game_developer",
        primaryKeys = arrayOf("gameId", "companyId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = GameDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = CompanyDao::class, parentColumns = arrayOf("id"), childColumns = arrayOf("companyId"), onDelete = ForeignKey.CASCADE))))
data class GameDeveloperDao(val gameId: Int, val companyId: Int)

//@Entity(tableName = "time_to_beat",
//        foreignKeys = arrayOf(ForeignKey(entity = GameDao::class,
//                parentColumns = arrayOf("id"),
//                childColumns = arrayOf("gameId"),
//                onDelete = ForeignKey.CASCADE)))


/**
val developers: List<Int>? = listOf(),
val publishers: List<Int>? = listOf(),
val game_engines: List<Int>? = listOf(),
val genres: List<Int>? = listOf(),
val first_release_date: Long? = null,
val release_dates: List<ReleaseDateDTO>? = listOf(),
val screenshots: List<ImageDTO>? = listOf(),
val videos: List<VideoDTO>? = listOf(),
val games: List<Int>? = listOf()*/