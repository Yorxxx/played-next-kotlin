package com.piticlistudio.playednext.ui.gamerelation.detail

import android.content.Context
import android.content.Intent
import com.piticlistudio.playednext.ui.ActivityArgs

/**
 * Arguments to launch [GameRelationDetailActivity]
 * Created by e-jegi on 3/23/2018.
 */
data class GameRelationActivityArgs(val gameId: Int): ActivityArgs {

    override fun intent(activity: Context): Intent = Intent(activity, GameRelationDetailActivity::class.java).apply {
        putExtra(GAME_ID, gameId)
    }

    companion object {
        fun deserializeFrom(intent: Intent): GameRelationActivityArgs {
            return GameRelationActivityArgs(intent.getIntExtra(GAME_ID, 0))
        }
    }
}

private const val GAME_ID = "game_id"