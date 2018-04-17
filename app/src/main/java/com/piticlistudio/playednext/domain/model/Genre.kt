package com.piticlistudio.playednext.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representation of a Genre
 */
@Parcelize
data class Genre(val id: Int, val name: String, val url: String? = null): Parcelable