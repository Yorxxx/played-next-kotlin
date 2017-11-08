package com.piticlistudio.playednext.util.ext

import android.content.Context
import android.graphics.Canvas
import android.widget.ImageView

class ScrollParallaxImageView constructor(ctx: Context) : ImageView(ctx) {

    private val SCALE_RATIO = 0.25f
    private val viewLocation = IntArray(2)

    override fun onDraw(canvas: Canvas?) {
        drawable?.apply {
            getLocationInWindow(viewLocation)
            applyScaleTransformation(canvas!!, viewLocation[0], viewLocation[1])
        }
        super.onDraw(canvas)
    }

    private fun applyScaleTransformation(canvas: Canvas, x: Int, y: Int) {

        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val deviceHeight = context.getScreenHeight()

        if (y == 0) {
            return
        }

        val scale = 2f * (1 - SCALE_RATIO) * (y + height).toFloat() / (deviceHeight + height) + SCALE_RATIO
        canvas.scale(scale, scale, (width / 2).toFloat(), (height / 2).toFloat())
    }
}