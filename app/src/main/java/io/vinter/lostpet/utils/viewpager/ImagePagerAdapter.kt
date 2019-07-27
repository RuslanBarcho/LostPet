package io.vinter.lostpet.utils.viewpager

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import io.vinter.lostpet.R
import io.vinter.lostpet.utils.GlideApp

class ImagePagerAdapter(private val pictureUrls: ArrayList<String>, val context: Context): PagerAdapter() {

    constructor(pictureUrls: ArrayList<String>, context: Context, listener: (position: Int) -> Unit) : this(pictureUrls, context) {
        this.listener = listener
    }

    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var listener: ((Int) -> Unit)? = null

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o as LinearLayout
    }

    override fun getCount(): Int {
        return pictureUrls.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.item_image, container, false)
        val imageView = itemView.findViewById(R.id.item_advert_image) as ImageView
        if (listener!= null) imageView.setOnClickListener { listener!!(position) }

        GlideApp.with(context)
                .load(pictureUrls[position])
                .error(R.drawable.placeholder)
                .transform(CenterCrop())
                .into(imageView)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}