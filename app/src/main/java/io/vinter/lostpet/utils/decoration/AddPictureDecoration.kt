package io.vinter.lostpet.utils.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class AddPictureDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {

        when {
            parent.getChildLayoutPosition(view) == 0 -> outRect.left = mItemOffset * 2
            else -> outRect.left = 0
        }
    }
}