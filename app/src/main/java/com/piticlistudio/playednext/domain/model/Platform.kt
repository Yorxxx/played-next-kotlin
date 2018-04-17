package com.piticlistudio.playednext.domain.model

import android.graphics.Color
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representation of a Platform
 */
@Parcelize
data class Platform(val id: Int,
                    val name: String,
                    val url: String? = null,
                    val createdAt: Long = 0,
                    val updatedAt: Long = 0) : Parcelable {

    var displayName: String? = null
        get() = field ?: name

    var displayColor: Int? = null
        get() = field ?: Color.BLACK
}