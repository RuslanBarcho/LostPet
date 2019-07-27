package io.vinter.lostpet.utils

import android.content.Context
import android.graphics.Rect
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View

class GridItemDecoration(private val mItemOffset: Int, private val columns: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int, columns: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), columns) {}

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when {
            parent.getChildLayoutPosition(view) in (0 until columns) -> outRect.set(mItemOffset, mItemOffset * 2, mItemOffset, mItemOffset * 2)
            else -> outRect.set(mItemOffset, 0, mItemOffset, mItemOffset * 2)
        }
    }
}
