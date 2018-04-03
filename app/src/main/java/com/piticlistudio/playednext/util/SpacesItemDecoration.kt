package com.piticlistudio.playednext.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacesItemDecoration(private val space: Int, private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.apply {
            left = space
            right = space
            bottom = space
        }

        parent?.let {
            if (parent.getChildLayoutPosition(view) >= spanCount) {
                outRect?.top = space
            }
            return
        }
        outRect?.top = 0
    }
}