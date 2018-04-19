package com.piticlistudio.playednext.ui.gamerelation.overview.mapper

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.piticlistudio.playednext.R
import com.piticlistudio.playednext.domain.model.GameRelation
import com.piticlistudio.playednext.domain.model.GameRelationStatus
import com.piticlistudio.playednext.ui.gamerelation.overview.model.RelationOverviewModel
import javax.inject.Inject

class RelationOverviewModelMapper @Inject constructor(private val context: Context) {

    fun mapIntoPresentationModel(model: List<GameRelation>, status: GameRelationStatus): RelationOverviewModel {
        val title: String?
        val count = model.count { it.status == status }
        val color: Int?
        val background: Drawable?
        when (status) {
            GameRelationStatus.BEATEN -> {
                title = context.getString(R.string.gamerelation_beaten_status)
                color = ContextCompat.getColor(context, R.color.gamerelation_beaten_color)
                background = context.getDrawable(R.drawable.shape_gradient_beaten)
            }
            GameRelationStatus.PLAYED -> {
                title = context.getString(R.string.gamerelation_played_status)
                color = ContextCompat.getColor(context, R.color.gamerelation_played_color)
                background = context.getDrawable(R.drawable.shape_gradient_played)
            }
            GameRelationStatus.UNPLAYED -> {
                title = context.getString(R.string.gamerelation_backlog_status)
                color = ContextCompat.getColor(context, R.color.gamerelation_backlog_color)
                background = context.getDrawable(R.drawable.shape_gradient_backlog)
            }
            GameRelationStatus.COMPLETED -> {
                title = context.getString(R.string.gamerelation_completed_status)
                color = ContextCompat.getColor(context, R.color.gamerelation_completed_color)
                background = context.getDrawable(R.drawable.shape_gradient_completed)
            }
            else -> {
                title = context.getString(R.string.gamerelation_playing_status)
                color = ContextCompat.getColor(context, R.color.gamerelation_current_color)
                background = context.getDrawable(R.drawable.shape_gradient_playing)
            }
        }
        return RelationOverviewModel(title, count, background, color)
    }
}