package com.piticlistudio.playednext.ui.gamerelation.overview.model

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt

/**
 * Model representing the view data to be displayed in this module
 */
data class RelationOverviewModel(val name: String, val count: Int, val background: Drawable? = null,
                                 @ColorInt val color: Int)