package com.piticlistudio.playednext.domain.model

import android.app.AlarmManager

/**
 * Representation of a game.
 * Created by jorge on 14/09/17.
 */
data class Game(val id: Int,
                val name: String,
                val createdAt: Long,
                val updatedAt: Long,
                val summary: String?,
                val storyline: String?,
                val url: String,
                val rating: Double?,
                val ratingCount: Int?,
                val aggregatedRating: Double?,
                val aggregatedRatingCount: Int?,
                val totalRating: Double?,
                val totalRatingCount: Int?,
                val releasedAt: Long?,
                val cover: Cover?,
                val timeToBeat: TimeToBeat?,
                var developers: List<Company>?,
                var publishers: List<Company>?,
                var genres: List<Genre>?,
                var collection: Collection?,
                var syncedAt: Long,
                var platforms: List<Platform>?,
                var images: List<GameImage>?) {

    val developersName: String?
        get() { return developers?.joinToString{ it.name } }
    val genresName: String?
        get() { return genres?.joinToString { it.name }}
    val publishersName: String?
        get() { return publishers?.joinToString { it.name }}


    fun isExpired() = System.currentTimeMillis() - syncedAt > AlarmManager.INTERVAL_DAY*15
}

data class Cover(val url: String, val width: Int?, val height: Int?) {

    val smallUrl: String = "http:${url}".replace("t_thumb", "t_cover_small")
    val bigUrl: String = "http:${url}".replace("t_thumb", "t_cover_big")
}

data class TimeToBeat(val hastly: Int?, val normally: Int?, val completely: Int?)