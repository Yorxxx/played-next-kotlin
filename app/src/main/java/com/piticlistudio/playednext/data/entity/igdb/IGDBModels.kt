package com.piticlistudio.playednext.data.entity.igdb

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
                   var collection: IGDBCollection? = null,
                   val franchise: Int? = null,
                   val hypes: Int? = 0,
                   val popularity: Double? = null,
                   val rating: Double? = null,
                   val rating_count: Int? = 0,
                   val aggregated_rating: Double? = null,
                   val aggregated_rating_count: Int? = 0,
                   val total_rating: Double? = null,
                   val total_rating_count: Int? = 0,
                   val developers: List<IGDBCompany>? = listOf(),
                   val publishers: List<IGDBCompany>? = listOf(),
                   val game_engines: List<Int>? = listOf(),
                   val time_to_beat: IGDBTimeToBeat?,
                   val genres: List<IGDBGenre>? = listOf(),
                   val first_release_date: Long? = null,
                   val release_dates: List<ReleaseDateDTO>? = listOf(),
                   val screenshots: List<IGDBImage>? = listOf(),
                   val videos: List<VideoDTO>? = listOf(),
                   val cover: IGDBImage? = null,
                   val games: List<Int>? = listOf(),
                   val platforms: List<IGDBPlatform>? = listOf())

open class BaseEnumeratedEntity(val id: Int, val name: String, val slug: String, val url: String?,
                                val created_at: Long, val updated_at: Long)

class IGDBCompany(id: Int, name: String, slug: String, url: String, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

class IGDBGenre(id: Int, name: String, slug: String, url: String, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

class IGDBCollection(id: Int, name: String, slug: String, url: String, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

class IGDBPlatform(id: Int, name: String, slug: String, url: String?, created_at: Long, updated_at: Long) : BaseEnumeratedEntity(id, name, slug, url, created_at, updated_at)

data class IGDBTimeToBeat(val hastly: Int?, val normally: Int?, val completely: Int?)

data class ReleaseDateDTO(val game: Int, val category: Int, val platform: Int?, val human: String)

data class IGDBImage(val url: String, val cloudinary_id: String?, val width: Int?, val height: Int?) {
    val mediumSizeUrl: String = "https:${clearHTTPPrefix(url)}".replace("t_thumb", "t_screenshot_med")

    protected fun clearHTTPPrefix(input: String): String {
        return input.removePrefix("http:").removePrefix("https:")
    }
}

data class VideoDTO(val name: String, val video_id: String)
