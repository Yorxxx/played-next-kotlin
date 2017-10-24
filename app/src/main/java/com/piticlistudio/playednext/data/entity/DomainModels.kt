package com.piticlistudio.playednext.data.entity


/**
 * Representation for a [GameDomainModel] fetched for the data layer
 */
class GameDomainModel(val id: Int,
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
                      val timeToBeat: TimeToBeatDomainModel?,
                      val cover: CoverDomainModel?)

data class TimeToBeatDomainModel(val hastly: Int?, val normally: Int?, val completely: Int?)

data class CoverDomainModel(val url: String, val width: Int?, val height: Int?)

data class CompanyDomainModel(val id: Int, val name: String, val slug: String, val url: String, val created_at: Long, val updated_at: Long)
