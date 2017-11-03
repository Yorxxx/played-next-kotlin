package com.piticlistudio.playednext.domain.model

data class GameRelation(var game: Game?, var platform: Platform?, val currentStatus: GameRelationStatus,
                        var createdAt: Long = System.currentTimeMillis(), var updatedAt: Long = System.currentTimeMillis())