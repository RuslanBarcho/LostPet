package io.vinter.lostpet.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.profile.adverts.UserAdverts
import io.vinter.lostpet.ui.profile.favs.FavoritesFragment
import io.vinter.lostpet.ui.settings.SettingsFragment
import io.vinter.lostpet.utils.GlideApp
import io.vinter.lostpet.utils.adapter.ProfileRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var mRootView: View

    @BindView(R.id.profile_picture)
    lateinit var profilePicture: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_profile, container, false)
        ButterKnife.bind(this, mRootView)
        GlideApp.with(context!!)
                .load("https://i.dailymail.co.uk/i/pix/2017/04/20/13/3F6B966D00000578-4428630-image-m-80_1492690622006.jpg")
                .override(300, 300)
                .placeholder(R.drawable.light_container)
                .error(R.drawable.light_container)
                .transforms(CenterCrop(), CircleCrop())
                .into(profilePicture)

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    }

}
