package com.piticlistudio.playednext.data.entity.room

import android.arch.persistence.room.*
import com.piticlistudio.playednext.domain.model.Collection
import com.piticlistudio.playednext.domain.model.Company
import com.piticlistudio.playednext.domain.model.GameImage
import com.piticlistudio.playednext.domain.model.Genre
import com.piticlistudio.playednext.domain.model.Platform

@Entity(tableName = "game")
data class RoomGame(@PrimaryKey val id: Int,
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
                    @Embedded val timeToBeat: RoomTimeToBeat?,
                    @Embedded val cover: RoomImage?,
                    @ColumnInfo(name = "synced_at") val syncedAt: Long)

data class RoomGameProxy(val game: RoomGame,
                         var developers: List<Company> = listOf(),
                         val publishers: List<Company> = listOf(),
                         val genres: List<Genre> = listOf(),
                         val images: List<GameImage> = listOf(),
                         val platforms: List<Platform> = listOf(),
                         val collection: Collection? = null)

data class RoomTimeToBeat(@ColumnInfo(name = "beat_hastly") val hastly: Int?,
                          @ColumnInfo(name = "beat_normally") val normally: Int?,
                          @ColumnInfo(name = "beat_completely") val completely: Int?)

data class RoomImage(@ColumnInfo(name = "cover_url") val url: String,
                     @ColumnInfo(name = "cover_width") val width: Int?,
                     @ColumnInfo(name = "cover_height") val height: Int?)

@Entity(tableName = "company")
data class RoomCompany(@PrimaryKey val id: Int, val name: String, val url: String?)

@Entity(tableName = "genre")
data class RoomGenre(@PrimaryKey val id: Int, val name: String, val url: String?)

@Entity(tableName = "collection")
data class RoomCollection(@PrimaryKey val id: Int, val name: String, val url: String?)

@Entity(tableName = "platform")
data class RoomPlatform(@PrimaryKey val id: Int, val name: String, val abbreviation: String?, val url: String?, val created_at: Long, val updated_at: Long)

@Entity(tableName = "game_developer",
        primaryKeys = arrayOf("gameId", "companyId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomCompany::class, parentColumns = arrayOf("id"), childColumns = arrayOf("companyId"), onDelete = ForeignKey.CASCADE))))
data class RoomGameDeveloper(val gameId: Int, val companyId: Int)

@Entity(tableName = "game_publisher",
        primaryKeys = arrayOf("gameId", "companyId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomCompany::class, parentColumns = arrayOf("id"), childColumns = arrayOf("companyId"), onDelete = ForeignKey.CASCADE))))
data class RoomGamePublisher(val gameId: Int, val companyId: Int)

@Entity(tableName = "game_genre",
        primaryKeys = arrayOf("gameId", "genreId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomGenre::class, parentColumns = arrayOf("id"), childColumns = arrayOf("genreId"), onDelete = ForeignKey.CASCADE))))
data class RoomGameGenre(val gameId: Int, val genreId: Int)

@Entity(tableName = "game_collection",
        primaryKeys = arrayOf("gameId", "collectionId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomCollection::class, parentColumns = arrayOf("id"), childColumns = arrayOf("collectionId"), onDelete = ForeignKey.CASCADE))))
data class RoomGameCollection(val gameId: Int, val collectionId: Int)

@Entity(tableName = "game_platform",
        primaryKeys = arrayOf("gameId", "platformId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomPlatform::class, parentColumns = arrayOf("id"), childColumns = arrayOf("platformId"), onDelete = ForeignKey.CASCADE))))
data class RoomGamePlatform(val gameId: Int, val platformId: Int)

@Entity(tableName = "game_relation",
        primaryKeys = arrayOf("gameId", "platformId"),
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE)),
                (ForeignKey(entity = RoomPlatform::class, parentColumns = arrayOf("id"), childColumns = arrayOf("platformId"), onDelete = ForeignKey.CASCADE))
        ))
data class GameRelationDao(val gameId: Int, val platformId: Int, val status: Int, val created_at: Long, val updated_at: Long)

@Entity(tableName = "game_screenshots",
        foreignKeys = arrayOf(
                (ForeignKey(entity = RoomGame::class, parentColumns = arrayOf("id"), childColumns = arrayOf("gameId"), onDelete = ForeignKey.CASCADE))
        ))
data class RoomGameImage(@Embedded val image: RoomImage, @PrimaryKey(autoGenerate = true) var id: Int? = null, val gameId: Int)