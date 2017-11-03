package com.piticlistudio.playednext.domain.model

data class GameRelation(val game: Game?, val platform: Platform?, val currentStatus: GameRelationStatus,
                        var createdAt: Long = System.currentTimeMillis(), var updatedAt: Long = System.currentTimeMillis())