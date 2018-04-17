package com.piticlistudio.playednext.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Entity that defines how long it takes to complete a game
 * Created by e-jegi on 4/5/2018.
 */
@Parcelize
data class TimeToBeat(val quick: Int?, val normally: Int?, val completely: Int?): Parcelable