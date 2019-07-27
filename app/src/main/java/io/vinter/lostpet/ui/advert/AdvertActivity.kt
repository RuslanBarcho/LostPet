package io.vinter.lostpet.ui.advert

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tbruyelle.rxpermissions2.RxPermissions
import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.Location
import io.vinter.lostpet.ui.advert.image.ImageViewActivity
import io.vinter.lostpet.ui.edit.EditActivity
import io.vinter.lostpet.utils.GlideApp
import io.vinter.lostpet.utils.config.StyleApplicator
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
        val id = intent.getStringExtra("advertId")

        detail_advert_back.setOnClickListener {
            this.finish()
        }

        if (viewModel.advert.value == null) viewModel.getAdvertDetail(preferences.getString("token", "")!!, id)

        viewModel.advert.observe(this, Observer {detail ->
            if (detail != null){
                detail_advert_name.text = detail.advertTitle
                detail_advert_description.text = detail.advertDescription
                detail_adver_owner_name.text = detail.owner!!.name
                setAdvertTypes(detail.animalType, detail.advertType)

                if (detail.pictureUrl != null){
                    if (detail.pictureUrl!!.size == 0){
                        configEmptyPager()
                    } else {
                        detail_advert_image.adapter = ImagePagerAdapter(detail.pictureUrl!!, this) {
                            val intent = Intent(this, ImageViewActivity::class.java)
                            intent.putExtra("data", detail.pictureUrl!!)
                            startActivity(intent)
                        }
                    }
                } else {
                    configEmptyPager()
                }

                if (detail.owner!!.id == preferences.getString("id", "")!!) {
                    detail_advert_action.setImageResource(R.drawable.ic_edit)
                    userAd = true
                }

                if (detail.isFavorite!!) detail_advert_action.setImageResource(R.drawable.ic_featured_filled)
                if (detail.owner?.pictureURL != null) loadUserImage(detail.owner!!.pictureURL!!)

                if (detail.location != null){
                    detail_location.text = detail.location!!.address
                    detail_location.visibility = View.VISIBLE
                    detail_location.setOnClickListener{openMap(detail.location!!, detail.advertTitle!!)}
                }

                detail_advert_action.setOnClickListener {
                    if (userAd){
                        val startEdit = Intent(this, EditActivity::class.java)
                        startEdit.putExtra("data", Advert(detail))
                        startActivityForResult(startEdit, 20)
                    } else if (!viewModel.advert.value!!.isFavorite!!) viewModel.addToFavorites(preferences.getString("token", "")!!, id)
                    else if (viewModel.advert.value!!.isFavorite!!) viewModel.deleteFromFavs(preferences.getString("token", "")!!, id)
                }

                detail_advert_call.setOnClickListener { _ ->
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        val rxPermissions = RxPermissions(this)
                        rxPermissions.request(Manifest.permission.CALL_PHONE,
                                Manifest.permission.CALL_PHONE)
                                .subscribe { granted ->
                                    if (granted) {
                                        makeCall(detail.owner!!.phoneNumber!!)
                                    }
                                }
                    } else {
                        makeCall(detail.owner!!.phoneNumber!!)
                    }
                }

                detail_advert_mesage.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", detail.owner!!.phoneNumber!!, null)))
                }
            }
        })

        viewModel.message.observe(this, Observer {
            if (it != null){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                viewModel.advert.value?.isFavorite = !viewModel.advert.value?.isFavorite!!
                if (viewModel.advert.value?.isFavorite!!) detail_advert_action.setImageResource(R.drawable.ic_featured_filled)
                else {
                    detail_advert_action.setImageResource(R.drawable.ic_favourites_outline)
                    val resultIntent = Intent()
                    resultIntent.putExtra("id", id)
                    setResult(33, resultIntent)
                }
                viewModel.message.postValue(null)
            }
        })
    }

    private fun loadUserImage(url: String){
        GlideApp.with(this)
                .load(url)
                .override(64, 64)
                .transforms(CenterCrop(), CircleCrop())
                .into(detail_advert_owner_image)
    }

    @SuppressLint("MissingPermission")
    private fun makeCall(number: String) {
        var phoneString = number
        if (number[0] == '7') phoneString = "+$phoneString"
        val phoneIntent = Intent(Intent.ACTION_CALL)
        phoneIntent.data = Uri.parse("tel:$phoneString")
        startActivity(phoneIntent)
    }

    private fun configEmptyPager(){
        val list = ArrayList<String>()
        list.add("")
        detail_advert_image.adapter = ImagePagerAdapter(list, this)
    }

    private fun openMap(location: Location, name: String){
        val gmUri = Uri.parse("geo:0,0?q=${location.latitude},${location.longitude}($name)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) startActivity(mapIntent)
    }

    private fun setAdvertTypes(animal: String?, advert: String?){
        when (animal){
            "cat" -> detail_advert_animal_type.text = resources.getStringArray(R.array.types)[0]
            "dog" -> detail_advert_animal_type.text = resources.getStringArray(R.array.types)[1]
        }
        when (advert){
            "missed" -> detail_advert_type.text = resources.getStringArray(R.array.post_types)[0]
            "found" -> detail_advert_type.text = resources.getStringArray(R.array.post_types)[1]
            "good-hands" -> detail_advert_type.text = resources.getStringArray(R.array.post_types)[2]
        }
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
