package com.piticlistudio.playednext.util.ext

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 * Created by e-jegi on 16/11/2017.
 */

object UIUtils {

    private var screenHeight = 0

    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            (c.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay?.let {
                val size = Point()
                it.getSize(size)
                screenHeight = size.y
            }
        }
        return screenHeight
    }
}