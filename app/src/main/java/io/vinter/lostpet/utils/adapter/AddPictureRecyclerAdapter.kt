package io.vinter.lostpet.utils.adapter

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
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
        if (i == 0){
            holder.image.setImageResource(R.drawable.ic_picture_add)
            holder.image.setOnClickListener {
                listener(i)
            }
            holder.remove.visibility = View.INVISIBLE
        } else {
            displayImage(holder.image, files[i - 1], context)
            holder.remove.setOnClickListener {
                listener(i)
            }
            holder.remove.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return files.size + 1
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.add_pic_item)
        val remove: ImageButton = itemView.findViewById(R.id.add_pic_remove)
    }

    fun removeItem(pos: Int, newList: ArrayList<Uri>){
        files = newList
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, files.size + 1)
        notifyItemChanged(0)
    }

    private fun displayImage(v: ImageView, u: Uri, context: Context){
        GlideApp.with(context)
                .load(RealPathUtil.getPathFromUri(context, u))
                .transforms(CenterCrop(), RoundedCorners(15))
                .into(v)
    }

}
