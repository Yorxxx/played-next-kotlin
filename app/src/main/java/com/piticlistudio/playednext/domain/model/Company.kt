package com.piticlistudio.playednext.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representation of a Company
 */
@Parcelize
data class Company(val id: Int, val name: String, val url: String?): Parcelable