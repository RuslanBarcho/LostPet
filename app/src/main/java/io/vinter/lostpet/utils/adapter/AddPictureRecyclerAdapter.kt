package io.vinter.lostpet.utils.adapter

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import io.vinter.lostpet.R
import io.vinter.lostpet.utils.GlideApp
import io.vinter.lostpet.utils.RealPathUtil

class AddPictureRecyclerAdapter(private var files: ArrayList<Uri>, private val context: Context, val listener: (position: Int) -> Unit) : RecyclerView.Adapter<AddPictureRecyclerAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView = inflater.inflate(R.layout.item_add_photo, viewGroup, false)
        return PictureViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, i: Int) {
        if (i < files.size){
            displayImage(holder.image, files[i], context)
            holder.remove.setOnClickListener {
                listener(i)
            }
            holder.remove.visibility = View.VISIBLE
        } else {
            holder.image.setImageResource(R.drawable.ic_picture_add)
            holder.image.setOnClickListener {
                listener(i)
            }
            holder.remove.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        if (files.size >=3 ) return files.size
        return files.size + 1
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.add_pic_item)
        val remove: ImageButton = itemView.findViewById(R.id.add_pic_remove)
    }

    fun removeItem(pos: Int, newList: ArrayList<Uri>){
        files = newList
        notifyItemRemoved(pos)
        notifyItemRangeChanged(0, files.size + 1)
        notifyItemChanged(files.size)
    }

    private fun displayImage(v: ImageView, u: Uri, context: Context){
        val realPath = RealPathUtil.getRealPathFromURI_API19(context, u)
        GlideApp.with(context)
                .load(realPath)
                .transforms(CenterCrop(), RoundedCorners(15))
                .into(v)
    }

}
