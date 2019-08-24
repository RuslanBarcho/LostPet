package io.vinter.lostpet.utils.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.load.resource.bitmap.CenterCrop

import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.utils.GlideApp

class AnimalRecyclerAdapter(private val adverts: ArrayList<Advert>, private val context: Context, val listener: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val LOADER = 1
    private var showLoader = false

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return if (i == LOADER) {
            val itemView = inflater.inflate(R.layout.loader, viewGroup, false)
            LoaderViewHolder(itemView)
        } else {
            val itemView = inflater.inflate(R.layout.item_animal, viewGroup, false)
            AnimalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(animalViewHolder: RecyclerView.ViewHolder, i: Int) {
        if (animalViewHolder is AnimalViewHolder){
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
    }

    override fun getItemCount(): Int {
        if (!showLoader) return adverts.size
        return adverts.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == adverts.size) return 1
        return super.getItemViewType(position)
    }

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.item_pic)
        var name: TextView = itemView.findViewById(R.id.item_descr)
    }

    class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addItems(adverts: ArrayList<Advert>){
        val beginIndex = this.adverts.size
        removeLoader()
        this.adverts.addAll(adverts)
        this.notifyItemRangeInserted(beginIndex, this.adverts.size)
    }

    fun getLastId(): String {
        return adverts.last().id!!
    }

    fun addLoader(){
        if (!showLoader){
            showLoader = true
            this.notifyItemInserted(adverts.size)
        }
    }

    fun removeLoader(){
        if (showLoader){
            showLoader = false
            notifyItemRemoved(this.adverts.size)
        }
    }
}
