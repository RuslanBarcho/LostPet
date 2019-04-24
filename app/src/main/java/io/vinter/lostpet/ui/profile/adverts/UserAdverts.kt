package io.vinter.lostpet.ui.profile.adverts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.profile.ProfileFragment
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
                var column = 2
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) column = 3
                val adverts = it
                adverts.reverse()
                val adapter = AnimalRecyclerAdapter(adverts, context!!) { id ->

                }
                user_adverts_recycler.layoutManager = GridLayoutManager(context, column)
                val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
                user_adverts_recycler.layoutAnimation = animation
                user_adverts_recycler.adapter = adapter
            }
        })

        user_adverts_back.setOnClickListener {
            (activity as MainActivity).changeProfilePage(ProfileFragment())
        }

    }

}
