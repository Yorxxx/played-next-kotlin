package com.piticlistudio.playednext.data.entity


/**
 * Representation for a [GameEntity] fetched for the data layer
 */
class GameEntity(val id: Int,
                 val name: String,
                 val url: String,
                 val createdAt: Long,
                 val updatedAt: Long,
                 val summary: String?,
                 val storyline: String?,
                 val collectionId: Int?,
                 val franchiseId: Int?,
                 val hypes: Int?,
                 val popularity: Double?,
                 val rating: Double?,
                 val ratingCount: Int? = 0,
                 val aggregatedRating: Double?,
                 val aggregatedRatingCount: Int?,
                 val totalRating: Double?,
                 val totalRatingCount: Int?,
                 val firstReleaseAt: Long?,
                 val timeToBeat: TimeToBeatEntity?,
                 val cover: CoverEntity?)

data class TimeToBeatEntity(val hastly: Int?, val normally: Int?, val completely: Int?)

data class CoverEntity(val url: String, val width: Int?, val height: Int?)
