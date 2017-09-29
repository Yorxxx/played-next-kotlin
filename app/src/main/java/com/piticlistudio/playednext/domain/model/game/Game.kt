package com.piticlistudio.playednext.domain.model.game

/**
 * Representation of a game.
 * Created by jorge on 14/09/17.
 */
data class Game(val id: Int, val name: String, val summary: String?, val storyline: String?,
                val url: String, val rating: Double?, val ratingCount: Int?,
                val aggregatedRating: Double?, val aggregatedRatingCount: Int?,
                val totalRating: Double?, val totalRatingCount: Int?,
                val releasedAt: Long?, val cover: Cover?, val timeToBeat: TimeToBeat?)

data class Cover(val url: String, val width: Int?, val height: Int?)

data class TimeToBeat(val quick: Int?, val normally: Int?, val completely: Int?)
