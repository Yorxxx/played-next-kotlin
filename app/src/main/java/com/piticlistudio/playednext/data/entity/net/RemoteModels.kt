package com.piticlistudio.playednext.data.entity.net

/**
 * Representation of a Remote entities
 */
data class GameRemote(val id: Int,
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
                      val time_to_beat: TimeToBeatRemote?,
                      val genres: List<Int>? = listOf(),
                      val first_release_date: Long? = null,
                      val release_dates: List<ReleaseDateRemote>? = listOf(),
                      val screenshots: List<ImageRemote>? = listOf(),
                      val videos: List<VideoRemote>? = listOf(),
                      val cover: ImageRemote? = null,
                      val games: List<Int>? = listOf())

data class CollectionRemote(val id: Int, val name: String, val slug: String, val url: String,
                            val created_at: Long, val updated_at: Long)


data class CompanyRemote(val id: Int, val name: String, val slug: String, val url: String,
                         val created_at: Long, val updated_at: Long)


data class FranchiseRemote(val id: Int, val name: String, val slug: String, val url: String,
                           val created_at: Long, val updated_at: Long)

data class TimeToBeatRemote(val hastly: Int?, val normally: Int?, val completely: Int?)

data class ReleaseDateRemote(val game: Int, val category: Int, val platform: Int?, val human: String)

data class ImageRemote(val url: String, val cloudinary_id: String?, val width: Int?, val height: Int?)

data class VideoRemote(val name: String, val video_id: String)

data class ExternalRemote(val steam: String? = null)


