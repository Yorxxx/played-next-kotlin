package com.piticlistudio.playednext.ui

import android.content.Context
import android.content.Intent

interface ActivityArgs {

    /**
     * Returns an [Intent] that can be used to lauch this activity
     */
    fun intent(activity: Context): Intent

    /**
     * Launches the activity given the supplied [activity] context
     * The default implementation uses the intent generated from [intent]
     */
    fun launch(activity: Context) = activity.startActivity(intent(activity))
}