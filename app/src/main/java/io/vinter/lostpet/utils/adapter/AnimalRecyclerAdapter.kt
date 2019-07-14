package io.vinter.lostpet.utils.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.load.resource.bitmap.CenterCrop

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

        var pictureUrl = ""
        if (adverts[i].puctureUrl!!.size > 0) pictureUrl = adverts[i].puctureUrl!![0]

        GlideApp.with(context)
                .load(pictureUrl)
                .override(300, 300)
                .placeholder(R.drawable.light_container)
                .error(R.drawable.placeholder)
                .transform(CenterCrop())
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

    fun addItems(adverts: ArrayList<Advert>){
        val beginIndex = this.adverts.size
        this.adverts.addAll(adverts)
        this.notifyItemRangeInserted(beginIndex, this.adverts.size)
    }

    fun getLastId(): String {
        return adverts.last().id!!
    }
}
