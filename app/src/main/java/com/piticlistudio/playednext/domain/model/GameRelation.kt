package com.piticlistudio.playednext.domain.model

/**
 * Representation of a relation with a game and platform.
 */
data class GameRelation(val game: Game,
                        val platform: Platform,
                        var status: GameRelationStatus = GameRelationStatus.NONE,
                        val createdAt: Long = System.currentTimeMillis(),
                        var updatedAt: Long = System.currentTimeMillis())