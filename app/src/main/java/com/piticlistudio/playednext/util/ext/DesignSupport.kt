package com.piticlistudio.playednext.util.ext

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView


fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackbar(@StringRes() text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun Fragment.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return getView()!!.snackbar(text, duration, init)
}

fun Fragment.snackbar(@StringRes() text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return getView()!!.snackbar(text, duration, init)
}

fun Activity.snackbar(view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view.snackbar(text, duration, init)
}

fun Activity.snackbar(view: View, @StringRes() text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view.snackbar(text, duration, init)
}

fun FragmentActivity.setContentFragment(containerViewId: Int, f: () -> Fragment): Fragment? {
    val manager = supportFragmentManager

    return f().apply {
        manager?.beginTransaction()?.replace(containerViewId, this)?.addToBackStack(null)?.commit()
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

fun Context.getScreenHeight(): Int {
    (this.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay?.let {
        val size = Point()
        it.getSize(size)
        return size.y
    }
    return 0
}
