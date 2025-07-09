package com.yesitlabs.jumballapp.adapter

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.Target
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.loader.visibility = View.GONE // Hide loader on error
                            return false // Allow Glide to handle setting the error image
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.loader.visibility = View.GONE // Hide loader on success
                            return false // Allow Glide to handle setting the image
                        }
                    })
                    .into(binding.stickerImg)
            }else{
                binding.loader.visibility = View.GONE // Hide loader on success
            }
        }
    }
}
