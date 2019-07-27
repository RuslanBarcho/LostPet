package io.vinter.lostpet.utils.viewpager

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class HackyViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null): ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            //e.printStackTrace();
            false
        }
    }
}