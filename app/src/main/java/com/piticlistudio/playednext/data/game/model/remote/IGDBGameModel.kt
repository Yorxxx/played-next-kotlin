package com.piticlistudio.playednext.data.game.model.remote

/**
 * Representation of a Game by IGDB
 */
class IGDBGameModel(val id: Int, val name: String, val summary: String?, val storyline: String?,
                    val collection: Int?, val franchise: Int?, val rating: Float?, val developers: List<Int>? = listOf(),
                    val publishers: List<Int>? = listOf(), val genres: List<Int>? = listOf(),
                    val first_release_date: Long? = 0)