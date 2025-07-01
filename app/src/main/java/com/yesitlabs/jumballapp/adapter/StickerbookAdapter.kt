package com.yesitlabs.jumballapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yesitlabs.jumballapp.AppConstant.Companion.STICKER_URL
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.databinding.ItemStickerbookBinding

class StickerbookAdapter(
    private val datalist: ArrayList<String>,
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<StickerbookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStickerbookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position], position, requireActivity)
    }

    override fun getItemCount(): Int = datalist.size

    class ViewHolder(private val binding: ItemStickerbookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sticker: String, position: Int, requireActivity: FragmentActivity) {

            if (position == 0 || position == 3) {
                binding.stickerImg.visibility = View.INVISIBLE
            } else {
                binding.stickerImg.visibility = View.VISIBLE
            }

            if (sticker.isNotEmpty()) {
                Glide.with(requireActivity)
                    .load("$STICKER_URL$sticker")
                    .placeholder(R.drawable.s_image)
                    .error(R.drawable.s_image)
                    .into(binding.stickerImg)
            }
        }
    }
}
