package com.piticlistudio.playednext.data.entity.net

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/**
 * Representation of a Remote entities
 */
data class GameDTO(val id: Int,
                   val name: String,
                   val slug: String,
                   val url: String,
                   val created_at: Long,
                   val updated_at: Long,
                   val summary: String? = null,
                   val storyline: String? = null,
                   val collection: Int? = null,
                   val franchise: Int? = null,
                   val hypes: Int? = 0,
                   val popularity: Double? = null,
                   val rating: Double? = null,
                   val rating_count: Int? = 0,
                   val aggregated_rating: Double? = null,
                   val aggregated_rating_count: Int? = 0,
                   val total_rating: Double? = null,
                   val total_rating_count: Int? = 0,
                   val developers: List<Int>? = listOf(),
                   val publishers: List<Int>? = listOf(),
                   val game_engines: List<Int>? = listOf(),
                   val time_to_beat: TimeToBeatDTO?,
                   val genres: List<Int>? = listOf(),
                   val first_release_date: Long? = null,
                   val release_dates: List<ReleaseDateDTO>? = listOf(),
                   val screenshots: List<ImageDTO>? = listOf(),
                   val videos: List<VideoDTO>? = listOf(),
                   val cover: ImageDTO? = null,
                   val games: List<Int>? = listOf())

data class CollectionDTO(val id: Int, val name: String, val slug: String, val url: String,
                            val created_at: Long, val updated_at: Long)


data class CompanyDTO(val id: Int, val name: String, val slug: String, val url: String,
                         val created_at: Long, val updated_at: Long)


data class FranchiseDTO(val id: Int, val name: String, val slug: String, val url: String,
                           val created_at: Long, val updated_at: Long)

data class TimeToBeatDTO(val hastly: Int?, val normally: Int?, val completely: Int?)

data class ReleaseDateDTO(val game: Int, val category: Int, val platform: Int?, val human: String)

data class ImageDTO(val url: String, val cloudinary_id: String?, val width: Int?, val height: Int?)

data class VideoDTO(val name: String, val video_id: String)

data class ExternalRemote(val steam: String? = null)
