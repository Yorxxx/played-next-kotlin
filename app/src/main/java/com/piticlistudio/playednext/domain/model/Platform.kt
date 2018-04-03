package com.piticlistudio.playednext.domain.model

import android.graphics.Color

/**
 * Representation of a Platform
 */
data class Platform(val id: Int,
                    val name: String,
                    val slug: String? = null,
                    val url: String? = null,
                    val createdAt: Long = 0,
                    val updatedAt: Long = 0) {

    var displayName: String? = null
        get() = field ?: name

    var displayColor: Int? = null
        get() = field ?: Color.BLACK
}