package com.piticlistudio.playednext.domain.model

data class GameRelation(private val game: Game, private val platform: Platform, private val currentStatus: GameRelationStatus,
                        private var createdAt: Long = System.currentTimeMillis(), private var updatedAt: Long = System.currentTimeMillis())