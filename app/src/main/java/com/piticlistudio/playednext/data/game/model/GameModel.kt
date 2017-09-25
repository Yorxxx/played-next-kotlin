package com.piticlistudio.playednext.data.game.model

/**
 * Representation for a [GameModel] fetched for the data layer
 */
class GameModel(val id: Int, val name: String, val summary: String?, val storyline: String?,
                val collection: Int?, val franchise: Int?, val rating: Float?)