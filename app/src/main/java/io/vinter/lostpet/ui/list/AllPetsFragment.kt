package io.vinter.lostpet.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SearchView

import io.vinter.lostpet.R
import io.vinter.lostpet.network.form.FilterForm
import io.vinter.lostpet.ui.advert.AdvertActivity
import io.vinter.lostpet.ui.list.filter.FilterFragment
import io.vinter.lostpet.utils.GridItemDecoration
import io.vinter.lostpet.utils.adapter.AnimalRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_all_pets.*

/**
 * Фрагмент для отображения списка активных объявлений
 */
class AllPetsFragment : Fragment() {

    private lateinit var viewModel: AllPetsViewModel
    private lateinit var adapter: AnimalRecyclerAdapter
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_pets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllPetsViewModel::class.java)
        preferences = context!!.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        if (viewModel.adverts.value == null) viewModel.getAllAdverts(preferences.getString("token", "")!!)

        viewModel.adverts.observe(this, Observer {
            if (it != null){
                var column = 2
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) column = 3
                adapter = AnimalRecyclerAdapter(it, context!!) { id ->
                    val openDetail = Intent(activity, AdvertActivity::class.java)
                    openDetail.putExtra("advertId", id)
                    activity!!.startActivityForResult(openDetail, 23)
                }
                all_pets_recycler.layoutManager = GridLayoutManager(context, column)
                if (all_pets_recycler.itemDecorationCount == 0) all_pets_recycler.addItemDecoration(GridItemDecoration(context!!, R.dimen.item_offset, column))
                val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
                all_pets_recycler.layoutAnimation = animation
                all_pets_recycler.adapter = adapter
            }
        })

        pets_filter_button.setOnClickListener {
            val dialog = FilterFragment()
            dialog.setTargetFragment(this, 23)
            dialog.show(fragmentManager, "filter")
        }

        adverts_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                adverts_search.clearFocus()
                viewModel.searchAdverts(preferences.getString("token", "")!!, s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

    fun update(){
        viewModel.getAllAdverts(preferences.getString("token", "")!!)
    }

    fun filterList(filer: FilterForm){
        viewModel.getFilteredAdverts(preferences.getString("token", "")!!, filer)
    }

}
