package io.vinter.lostpet.ui.advert.image

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.viewpager.widget.ViewPager
import io.vinter.lostpet.R
import io.vinter.lostpet.utils.viewpager.FullSizeImagePagerAdapter
import kotlinx.android.synthetic.main.activity_image_view.*

class ImageViewActivity : AppCompatActivity() {

    private var pos = 0

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
        pos = savedInstanceState?.getInt("pos") ?: intent.getIntExtra("position", 0)

        val data = intent.getSerializableExtra("data") as ArrayList<String>

        image_view_full_size_pager.adapter = FullSizeImagePagerAdapter(data, this)
        if (savedInstanceState == null) image_view_full_size_pager.currentItem = pos

        image_view_full_counter.text = "${pos + 1} ${getString(R.string.of)} ${data.size}"
        image_view_full_size_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                image_view_full_counter.text = "${position + 1} ${getString(R.string.of)} ${data.size}"
                pos = position
            }

        })
        image_view_back.setOnClickListener { finish() }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt("pos", pos)
        super.onSaveInstanceState(outState, outPersistentState)
    }
}
