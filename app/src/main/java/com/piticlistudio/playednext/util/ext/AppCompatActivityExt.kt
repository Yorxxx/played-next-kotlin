package com.piticlistudio.playednext.util.ext

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.WindowManager

inline fun FragmentActivity.setContentFragment(containerViewId: Int, f: () -> Fragment): Fragment? {
    val manager = supportFragmentManager
    val fragment = manager?.findFragmentById(containerViewId)
    fragment?.let { return it }
    return f().apply {
        manager?.beginTransaction()?.add(containerViewId, this)?.commit()
    }
}

inline fun Activity.getScreenHeight(): Int {
    (this.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay?.let {
        val size = Point()
        it.getSize(size)
        return size.y
    }
    return 0
}