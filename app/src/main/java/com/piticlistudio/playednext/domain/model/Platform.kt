package com.piticlistudio.playednext.domain.model

import android.graphics.Color

/**
 * Representation of a Platform
 */
data class Platform(val id: Int, val name: String, val slug: String, val url: String?, val createdAt: Long, val updatedAt: Long) {

    var displayName: String? = null
        get() = field ?: name

    var displayColor: Int? = null
        get() = field ?: Color.BLACK
}