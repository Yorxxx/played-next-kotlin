package com.piticlistudio.playednext.domain.model

data class GameRelation(var game: Game?, var platform: Platform?, var currentStatus: GameRelationStatus = GameRelationStatus.NONE,
                        var createdAt: Long = System.currentTimeMillis(), var updatedAt: Long = System.currentTimeMillis())