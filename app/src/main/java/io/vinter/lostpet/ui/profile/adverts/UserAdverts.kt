package io.vinter.lostpet.ui.profile.adverts

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.advert.AdvertActivity
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.profile.ProfileFragment
import io.vinter.lostpet.utils.GridItemDecoration
import io.vinter.lostpet.utils.adapter.AnimalRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_user_adverts.*

class UserAdverts : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_adverts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(UserAdvertsViewModel::class.java)
        val preferences = context!!.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        if (viewModel.adverts.value == null) viewModel.getUserAdverts(preferences.getString("token", "")!!)

        viewModel.adverts.observe(this, Observer {
            if (it != null){
                user_adverts_loader.visibility = View.GONE
                var column = 2
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) column = 3
                val adapter = AnimalRecyclerAdapter(it.adverts!!, context!!) { id ->
                    val openDetail = Intent(activity, AdvertActivity::class.java)
                    openDetail.putExtra("advertId", id)
                    startActivityForResult(openDetail, 23)
                }
                favorites_recycler.layoutManager = GridLayoutManager(context, column)
                if (favorites_recycler.itemDecorationCount == 0) favorites_recycler.addItemDecoration(GridItemDecoration(context!!, R.dimen.item_offset, column))
                val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
                favorites_recycler.layoutAnimation = animation
                favorites_recycler.adapter = adapter
            }
        })

        user_adverts_back.setOnClickListener {
            (activity as MainActivity).changeProfilePage(ProfileFragment())
        }

    }

}
