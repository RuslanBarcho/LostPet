package io.vinter.lostpet.ui.settings

import android.Manifest
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tbruyelle.rxpermissions2.RxPermissions

import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.User
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.profile.ProfileFragment
import io.vinter.lostpet.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_settings.*
import android.app.Activity
import android.view.inputmethod.InputMethodManager

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        preferences = context!!.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

        settings_phone.hint = preferences.getString("phone_number", "")
        settings_name.hint = preferences.getString("name", "")
        val url = preferences.getString("pictureURL", "")
        if (url != "") displayImage(url!!)

        settings_confirm.setOnClickListener{
            val user = User()
            if (settings_name.text.toString() != "") user.name = settings_name.text.toString()
            if (settings_phone.text.toString() != "") user.phoneNumber = settings_phone.text.toString()
            if (user.name != null || user.phoneNumber != null) viewModel.editUser(preferences.getString("token", "")!!, user)
        }

        settings_back.setOnClickListener {
            (activity as MainActivity).changeProfilePage(ProfileFragment())
        }

        settings_confirm.setOnClickListener{
            hideSoftKeyboard()
            activity?.currentFocus?.clearFocus()
            val user = User()
            if (settings_name.text.toString() != "") user.name = settings_name.text.toString()
            if (settings_phone.text.toString() != "") user.phoneNumber = settings_phone.text.toString()
            if (user.name != null || user.phoneNumber != null) viewModel.editUser(preferences.getString("token", "")!!, user)
        }

        viewModel.userData.observe(this, Observer {
            if (it != null){
                Toast.makeText(context, "Обновлено", Toast.LENGTH_SHORT).show()
                preferences.edit().putString("name", it.name).apply()
                preferences.edit().putString("phone_number", it.phoneNumber).apply()
                settings_phone.hint = it.phoneNumber
                settings_name.hint = it.name
                viewModel.userData.postValue(null)
            }
        })

        viewModel.pictureUrl.observe(this, Observer {
            if (it != null){
                displayImage(it.message!!)
                preferences.edit().putString("pictureURL", it.message).apply()
            }
        })

        settings_add_picture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val rxPermissions = RxPermissions(activity!!)
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe { granted ->
                            if (granted) {
                                chooseAndUploadImage()
                            }
                        }
            } else {
                chooseAndUploadImage()
            }
        }
    }

    private fun displayImage(url: String){
        GlideApp.with(context!!)
                .load(url)
                .override(200, 200)
                .transforms(CenterCrop(), CircleCrop())
                .into(settings_preview_image)
    }

    private fun chooseAndUploadImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 44)
    }

    private fun hideSoftKeyboard(){
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            44 -> {
                if (resultCode == RESULT_OK && data != null) viewModel.uploadImage(preferences.getString("token", "")!!, context!!, data.data!!)
            }
        }
    }

}
