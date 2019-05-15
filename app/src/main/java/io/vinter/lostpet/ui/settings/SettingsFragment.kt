package io.vinter.lostpet.ui.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.User
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val preferences = context!!.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

        settings_phone.hint = preferences.getString("phone_number", "")
        settings_name.hint = preferences.getString("name", "")

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
            val user = User()
            if (settings_name.text.toString() != "") user.name = settings_name.text.toString()
            if (settings_phone.text.toString() != "") user.phoneNumber = settings_phone.text.toString()
            if (user.name != null || user.phoneNumber != null) viewModel.editUser(preferences.getString("token", "")!!, user)
        }

        viewModel.message.observe(this, Observer {
            if (it != null){
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                viewModel.message.postValue(null)
            }
        })
    }

}
