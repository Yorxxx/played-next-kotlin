package com.piticlistudio.playednext.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representation of a Collection
 */
@Parcelize
data class Collection(val id: Int, val name: String, val url: String?): Parcelable