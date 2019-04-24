package io.vinter.lostpet.ui.advert

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.ui.edit.EditActivity
import io.vinter.lostpet.utils.StyleApplicator
import io.vinter.lostpet.utils.viewpager.ImagePagerAdapter
import kotlinx.android.synthetic.main.activity_advert.*

class AdvertActivity : AppCompatActivity() {

    private var userAd = false
    private lateinit var viewModel: AdvertViewModel
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advert)
        StyleApplicator.style(this)
        viewModel = ViewModelProviders.of(this).get(AdvertViewModel::class.java)
        preferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

        detail_advert_back.setOnClickListener {
            this.finish()
        }

        if (viewModel.advert.value == null) viewModel.getAdvertDetail(preferences.getString("token", "")!!, intent.getStringExtra("advertId"))

        viewModel.advert.observe(this, Observer {detail ->
            if (detail != null){
                detail_advert_name.text = detail.advertTitle
                detail_advert_description.text = detail.advertDescription

                if (detail.pictureUrl != null){
                    if (detail.pictureUrl!!.size == 0){
                        configEmptyPager()
                    } else detail_advert_image.adapter = ImagePagerAdapter(detail.pictureUrl!!, this)
                } else {
                    configEmptyPager()
                }

                if (detail.owner!!.id == preferences.getString("id", "")!!) {
                    detail_advert_action.setImageResource(R.drawable.ic_edit)
                    userAd = true
                }

                detail_advert_action.setOnClickListener {
                    if (userAd) {
                        val startEdit = Intent(this, EditActivity::class.java)
                        startEdit.putExtra("data", Advert(detail))
                        startActivityForResult(startEdit, 20)
                    }
                }

                detail_advert_call.setOnClickListener { _ ->
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        val rxPermissions = RxPermissions(this)
                        rxPermissions.request(Manifest.permission.CALL_PHONE,
                                Manifest.permission.CALL_PHONE)
                                .subscribe { granted ->
                                    if (granted!!) {
                                        makeCall(detail.owner!!.phoneNumber!!)
                                    }
                                }
                    } else {
                        makeCall(detail.owner!!.phoneNumber!!)
                    }
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun makeCall(number: String) {
        val phoneIntent = Intent(Intent.ACTION_CALL)
        phoneIntent.data = Uri.parse("tel:$number")
        startActivity(phoneIntent)
    }

    private fun configEmptyPager(){
        val list = ArrayList<String>()
        list.add("https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
        detail_advert_image.adapter = ImagePagerAdapter(list, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            20 -> viewModel.getAdvertDetail(preferences.getString("token", "")!!, intent.getStringExtra("advertId"))
            21 -> {
                setResult(22)
                finish()
            }
        }
    }
}
