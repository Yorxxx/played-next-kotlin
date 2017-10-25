package com.piticlistudio.playednext.domain.model

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
                var collection: Collection?)

data class Cover(val url: String, val width: Int?, val height: Int?)

data class TimeToBeat(val hastly: Int?, val normally: Int?, val completely: Int?)