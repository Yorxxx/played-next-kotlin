package com.piticlistudio.playednext.data.game.model

/**
 * Representation for a [GameEntity] fetched for the data layer
 */
class GameEntity(val id: Int, val name: String, val summary: String?, val storyline: String?,
                 val collection: Int? = 0, val franchise: Int? = 0, val rating: Float? = 0f)