package io.vinter.lostpet.utils.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.vinter.lostpet.R

class ProfileRecyclerAdapter(var list: ArrayList<Int>, var context: Context, val listener: (Int) -> Unit): RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView = inflater.inflate(R.layout.item_profile, viewGroup, false)
        return ProfileViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        when (position){
            0 -> {
                holder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_posts))
                holder.name.text = context.getString(R.string.my_ads)
            }
            1 -> {
                holder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_featured))
                holder.name.text = context.getString(R.string.favorites)
            }
            2 -> {
                holder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_messages))
                holder.name.text = context.getString(R.string.messages)
            }
            3 -> {
                holder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_settings))
                holder.name.text = context.getString(R.string.settings)
            }
        }
        holder.itemView.setOnClickListener { listener(position) }
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var icon: ImageView = itemView.findViewById(R.id.profile_menu_icon)
        var name: TextView = itemView.findViewById(R.id.profile_menu_name)
    }
}