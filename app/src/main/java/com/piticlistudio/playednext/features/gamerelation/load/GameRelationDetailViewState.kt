package com.piticlistudio.playednext.features.gamerelation.load

import com.piticlistudio.playednext.domain.model.GameRelation

/**
 * Entity that defines the view state for GameRelation view.
 */
class GameRelationDetailViewState constructor(val data: GameRelation?, val loading: Boolean, val error: Throwable?)