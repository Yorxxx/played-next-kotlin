package com.piticlistudio.playednext.data.entity.net

import java.util.*

/**
 * Entities returned by Giantbomb API
 * Created by e-jegi on 3/26/2018.
 */
data class GiantbombListResponse<out T>(
        val error: String,
        val limit: Int,
        val offset: Int,
        val number_of_page_results: Int,
        val number_of_total_results: Int,
        val status_code: Int,
        val results: List<T> = listOf()
)

data class GiantbombEntityResponse<out T>(
        val error: String,
        val limit: Int,
        val offset: Int,
        val number_of_page_results: Int,
        val number_of_total_results: Int,
        val status_code: Int,
        val results: T?
)

data class GiantbombGame(val date_added: Date,
                         val date_last_updated: Date,
                         val deck: String? = null,
                         val description: String? = null,
                         val expected_release_day: Int? = null,
                         val expected_release_month: Int? = null,
                         val expected_release_quarter: Int? = null,
                         val expected_release_year: Int? = null,
                         val id: Int,
                         val image: GiantbombGameImage? = null,
                         val name: String,
                         val original_release_date: Date? = null,
                         val platforms: List<GiantbombPlatform>? = listOf(),
                         val images: List<GiantbombGameImage>? = listOf(),
                         val developers: List<GiantbombCompany>? = listOf(),
                         val publishers: List<GiantbombCompany>? = listOf(),
                         val franchises: List<GiantbombFranchise>? = listOf(),
                         val genres: List<GiantbombGenre>? = listOf())

data class GiantbombGameImage(val icon_url: String? = null,
                              val medium_url: String? = null,
                              val screen_url: String? = null,
                              val screen_large_url: String? = null,
                              val small_url: String? = null,
                              val super_url: String? = null,
                              val thumb_url: String? = null,
                              val tiny_url: String? = null,
                              val original_url: String? = null)

data class GiantbombPlatform(val id: Int, val name: String, val abbreviation: String? = null, val site_detail_url: String? = null)
data class GiantbombCompany(val id: Int, val name: String)
data class GiantbombFranchise(val id: Int, val name: String)
data class GiantbombGenre(val id: Int, val name: String, val site_detail_url: String? = null)