package com.piticlistudio.playednext.data.entity.net

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
                   var collection: CollectionDTO? = null,
                   val franchise: Int? = null,
                   val hypes: Int? = 0,
                   val popularity: Double? = null,
                   val rating: Double? = null,
                   val rating_count: Int? = 0,
                   val aggregated_rating: Double? = null,
                   val aggregated_rating_count: Int? = 0,
                   val total_rating: Double? = null,
                   val total_rating_count: Int? = 0,
                   val developers: List<CompanyDTO>? = listOf(),
                   val publishers: List<CompanyDTO>? = listOf(),
                   val game_engines: List<Int>? = listOf(),
                   val time_to_beat: TimeToBeatDTO?,
                   val genres: List<GenreDTO>? = listOf(),
                   val first_release_date: Long? = null,
                   val release_dates: List<ReleaseDateDTO>? = listOf(),
                   val screenshots: List<ImageDTO>? = listOf(),
                   val videos: List<VideoDTO>? = listOf(),
                   val cover: ImageDTO? = null,
                   val games: List<Int>? = listOf(),
                   val platforms: List<PlatformDTO>? = listOf())

open class BaseEnumeratedEntity(val id: Int, val name: String, val slug: String, val url: String?,
                                val created_at: Long, val updated_at: Long)

class CompanyDTO(id: Int, name: String, slug: String, url: String, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

class GenreDTO(id: Int, name: String, slug: String, url: String, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

class CollectionDTO(id: Int, name: String, slug: String, url: String, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

class PlatformDTO(id: Int, name: String, slug: String, url: String?, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

data class TimeToBeatDTO(val hastly: Int?, val normally: Int?, val completely: Int?)

data class ReleaseDateDTO(val game: Int, val category: Int, val platform: Int?, val human: String)

data class ImageDTO(val url: String, val cloudinary_id: String?, val width: Int?, val height: Int?)

data class VideoDTO(val name: String, val video_id: String)
