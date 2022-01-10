package com.example.forageapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.forageapp.data.Forage
import com.example.forageapp.databinding.ForageItemBinding
import androidx.recyclerview.widget.ListAdapter

class ForageListAdapter(private val onItemClicked:(Forage)->Unit):ListAdapter<Forage, ForageListAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private val binding:ForageItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(forage:Forage){
            binding.forageItemName.text = forage.name
            binding.forageItemLocation.text = forage.location
        }
    }

    companion object {
        private val DiffCallback = object:DiffUtil.ItemCallback<Forage>(){
            override fun areItemsTheSame(oldItem: Forage, newItem: Forage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Forage, newItem: Forage): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ForageItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }
}