package com.yesitlabs.jumballapp.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yesitlabs.jumballapp.AppConstant.Companion.MEDIA_URL
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.model.WorldCupModel


class WorldCupAdapter(var context: Context, private var wordlist: List<WorldCupModel>) :
    RecyclerView.Adapter<WorldCupAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_world_cup, parent, false)
        return VH(itemView)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: VH, position: Int) {

        val worldCupImage = holder.itemView.findViewById<ImageView>(R.id.wordcup_image)
        Glide.with(context).load(MEDIA_URL + wordlist[position].countryWorldCupFlag)
            .placeholder(
                context.resources.getDrawable(R.drawable.world_cup_pic , null)
            )
            .into(worldCupImage)


    }

    override fun getItemCount(): Int {
        return wordlist.size
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

//            fun bind(messageDetails:AboutData){
//
//                itemView.tv_profile_name_about_item.text = messageDetails.name
//                itemView.tv_about_text_item.text = messageDetails.text
//            }

    }
}