package com.piticlistudio.playednext.util.ext

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

inline fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}