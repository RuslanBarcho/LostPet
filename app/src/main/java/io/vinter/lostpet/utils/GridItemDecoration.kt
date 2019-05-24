package io.vinter.lostpet.utils

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

class GridItemDecoration(private val mItemOffset: Int, private val columns: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int, columns: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), columns) {}

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val rows = Math.ceil(state.itemCount.toDouble() / columns.toDouble()).toInt()
        val nonEmptyFields = columns - (rows * columns - state.itemCount)
        val beginBottom = state.itemCount - nonEmptyFields

        when {
            parent.getChildLayoutPosition(view) in (0 until columns) -> outRect.set(mItemOffset, mItemOffset * 2, mItemOffset, mItemOffset)
            parent.getChildLayoutPosition(view) in (beginBottom until state.itemCount) -> outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset * 2)
            else -> outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
        }
    }
}
