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
                val url: String?,
                val rating: Double?,
                val ratingCount: Int?,
                val aggregatedRating: Double?,
                val aggregatedRatingCount: Int?,
                val totalRating: Double?,
                val totalRatingCount: Int?,
                val releasedAt: Long?,
                val cover: Image?,
                val timeToBeat: TimeToBeat?,
                val developers: List<Company>,
                val publishers: List<Company>,
                val genres: List<Genre>,
                var collection: Collection?,
                var syncedAt: Long,
                var platforms: List<Platform>,
                var images: List<GameImage>) {

    val developersName: String?
        get() { return developers.joinToString{ it.name } }
    val genresName: String?
        get() { return genres.joinToString { it.name }}
    val publishersName: String?
        get() { return publishers.joinToString { it.name }}


    fun isExpired() = System.currentTimeMillis() - syncedAt > AlarmManager.INTERVAL_DAY*15
}

