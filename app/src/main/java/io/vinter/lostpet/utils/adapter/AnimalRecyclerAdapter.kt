package io.vinter.lostpet.utils.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.utils.GlideApp

class AnimalRecyclerAdapter(private val adverts: ArrayList<Advert>, private val context: Context, val listener: (String) -> Unit) : RecyclerView.Adapter<AnimalRecyclerAdapter.AnimalViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView = inflater.inflate(R.layout.item_animal, viewGroup, false)
        return AnimalViewHolder(itemView)
    }

    override fun onBindViewHolder(animalViewHolder: AnimalViewHolder, i: Int) {
        animalViewHolder.name.text = adverts[i].advertTitle

        var pictureUrl = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"
        if (adverts[i].puctureUrl!!.size > 0) pictureUrl = adverts[i].puctureUrl!![0]

        GlideApp.with(context)
                .load(pictureUrl)
                .override(300, 300)
                .placeholder(R.drawable.light_container)
                .error(R.drawable.light_container)
                .transforms(CenterCrop(), RoundedCorners(30))
                .into(animalViewHolder.image)
        animalViewHolder.itemView.setOnClickListener { listener(adverts[i].id!!) }
    }

    override fun getItemCount(): Int {
        return adverts.size
    }

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.item_pic)
        var name: TextView = itemView.findViewById(R.id.item_descr)
    }
}
