package com.yesitlabs.jumballapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yesitlabs.jumballapp.databinding.LayoutItemDyfBinding
import com.yesitlabs.jumballapp.model.ImageModel

class SubsItemViewPagerAdapter(private val listData: List<ImageModel>) :
    RecyclerView.Adapter<SubsItemViewPagerAdapter.SubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val binding = LayoutItemDyfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.bind(listData[position].Image)
    }

    override fun getItemCount(): Int = listData.size

    class SubViewHolder(private val binding: LayoutItemDyfBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageResId: Int) {
            binding.img.setImageResource(imageResId)
        }
    }
}
