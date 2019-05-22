package io.vinter.lostpet.ui.profile

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.profile.adverts.UserAdverts
import io.vinter.lostpet.ui.profile.favs.FavoritesFragment
import io.vinter.lostpet.ui.profile.settings.SettingsFragment
import io.vinter.lostpet.utils.GlideApp
import io.vinter.lostpet.utils.adapter.ProfileRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = context!!.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val menuItems = ArrayList<Int>()
        for (i in 0..3) menuItems.add(i)
        profile_recycler.layoutManager = GridLayoutManager(context, 2)
        profile_recycler.adapter = ProfileRecyclerAdapter(menuItems, context!!) {
            when (it) {
                0 -> (activity as MainActivity).changeProfilePage(UserAdverts())
                1 -> (activity as MainActivity).changeProfilePage(FavoritesFragment())
                3 -> (activity as MainActivity).changeProfilePage(SettingsFragment())
            }
        }
        val url = preferences.getString("pictureURL", "")
        if (url != "") displayImage(url!!)
    }

    private fun displayImage(url: String){
        GlideApp.with(context!!)
                .load(url)
                .override(200, 200)
                .transforms(CenterCrop(), CircleCrop())
                .into(profile_picture)
    }

}
