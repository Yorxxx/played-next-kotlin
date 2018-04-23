package com.piticlistudio.playednext.ui.playlists.overview.model

import android.support.annotation.ColorInt

/**
 * Model representing the view data to be displayed in this module
 */
data class PlaylistsOverviewModel(val name: String, val count: Int, @ColorInt val color: Int)