package io.vinter.lostpet.ui.advert.image

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import io.vinter.lostpet.R
import io.vinter.lostpet.utils.viewpager.FullSizeImagePagerAdapter
import kotlinx.android.synthetic.main.activity_image_view.*

class ImageViewActivity : AppCompatActivity() {

    override fun onStart() {
        overridePendingTransition(0,0)
        super.onStart()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        window.statusBarColor = resources.getColor(R.color.black)
        window.navigationBarColor = resources.getColor(R.color.black)
        val data = intent.getSerializableExtra("data") as ArrayList<String>
        image_view_full_size_pager.adapter = FullSizeImagePagerAdapter(data, this)

        image_view_full_counter.text = "1 ${getString(R.string.of)} ${data.size}"
        image_view_full_size_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                image_view_full_counter.text = "${position + 1} ${getString(R.string.of)} ${data.size}"
            }

        })
        image_view_back.setOnClickListener { finish() }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }
}
