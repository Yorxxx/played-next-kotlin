package com.piticlistudio.playednext.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Image constructor(val url: String, val width: Int? = 0, val height: Int? = 0): Parcelable