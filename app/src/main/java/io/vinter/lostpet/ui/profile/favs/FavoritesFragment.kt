package io.vinter.lostpet.ui.profile.favs

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
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: AnimalRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        val preferences = context!!.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        if (viewModel.adverts.value == null) viewModel.getFavorites(preferences.getString("token", "")!!)

        viewModel.adverts.observe(this, Observer {
            if (it != null){
                favorites_loader.visibility = View.GONE
                var column = 2
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) column = 3
                adapter = AnimalRecyclerAdapter(it.adverts!!, context!!) { id ->
                    val openDetail = Intent(activity, AdvertActivity::class.java)
                    openDetail.putExtra("advertId", id)
                    startActivityForResult(openDetail, 33)
                }
                favorites_recycler.layoutManager = GridLayoutManager(context, column)
                if (favorites_recycler.itemDecorationCount == 0)favorites_recycler.addItemDecoration(GridItemDecoration(context!!, R.dimen.item_offset, column))
                val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
                favorites_recycler.layoutAnimation = animation
                favorites_recycler.adapter = adapter
            }
        })

        favorites_back.setOnClickListener {
            (activity as MainActivity).changeProfilePage(ProfileFragment())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 33 && data!= null) {
            val toDelete = viewModel.adverts.value?.adverts?.first { it.id == data.getStringExtra("id") }
            val index = viewModel.adverts.value?.adverts?.indexOf(toDelete)
            if (index != null){
                val size = viewModel.adverts.value?.adverts?.size
                viewModel.adverts.value?.adverts?.removeAt(index)
                adapter.notifyItemRemoved(index)
                if (size != null) adapter.notifyItemRangeChanged(index, size)
            }
        }
    }

}
