package io.vinter.lostpet.ui.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        if (viewModel.adverts.value == null) viewModel.getAllAdverts(preferences.getString("token", "")!!, null)

        viewModel.adverts.observe(this, Observer {
            adverts_loader.visibility = View.GONE
            adverts_refresh.isRefreshing = false
            if (it != null){
                var column = 2
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) column = 3
                adapter = AnimalRecyclerAdapter(it.adverts!!, context!!) { id ->
                    val openDetail = Intent(activity, AdvertActivity::class.java)
                    openDetail.putExtra("advertId", id)
                    activity!!.startActivityForResult(openDetail, 23)
                }

                val layoutManager = GridLayoutManager(context, column)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter.getItemViewType(position)){
                            adapter.LOADER -> column
                            else -> 1
                        }
                    }
                }

                all_pets_recycler.layoutManager = layoutManager
                if (all_pets_recycler.itemDecorationCount == 0) all_pets_recycler.addItemDecoration(GridItemDecoration(context!!, R.dimen.item_offset, column))
                val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
                all_pets_recycler.layoutAnimation = animation
                all_pets_recycler.adapter = adapter

                all_pets_recycler.clearOnScrollListeners()
                all_pets_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val totalItemCount = layoutManager.itemCount
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        if (!viewModel.loading && totalItemCount <= lastVisibleItem + 1 && it.count!! > totalItemCount) {
                            recyclerView.post {adapter.addLoader()}
                            viewModel.refresh(preferences.getString("token", "")!!, adapter.getLastId())
                        }
                    }
                })
            }
        })

        viewModel.addictionAdverts.observe(this, Observer {
            if (it != null) adapter.addItems(it.adverts!!)
        })

        pets_filter_button.setOnClickListener {
            val dialog = FilterFragment()
            dialog.setTargetFragment(this, 23)
            dialog.show(fragmentManager, "filter")
        }

        adverts_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                adverts_search.clearFocus()
                viewModel.searchAdverts(preferences.getString("token", "")!!, s, null)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        adverts_refresh.setOnRefreshListener { update() }
    }

    fun update(){
        viewModel.refresh(preferences.getString("token", "")!!, null)
    }

    fun filterList(filer: FilterForm){
        viewModel.getFilteredAdverts(preferences.getString("token", "")!!, filer, null)
    }

    override fun onResume() {
        super.onResume()
        adverts_search.clearFocus()
    }

}