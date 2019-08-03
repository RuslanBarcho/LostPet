package io.vinter.lostpet.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.vinter.lostpet.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs) {
    init {
        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_error, this)
    }

    fun setOnRetryListener(listener: () -> Unit){
        erroe_view_retry.setOnClickListener { listener() }
    }
}