package com.example.warrantyreminder.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.utils.Utils
import kotlinx.android.synthetic.main.fragment_warranty_item.view.*

class SearchListAdapter(var searchList: List<WarrantyItem>) :
    RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {

    class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(warrantyItem: WarrantyItem) {
            itemView.tvItemName.text = warrantyItem.itemName
            itemView.tvItemDescription.text = warrantyItem.itemDescription
            itemView.tvExpiryDate.text = Utils.convertMillisToString(warrantyItem.expirationDate)

            if (warrantyItem.imageUrl.isEmpty()) {
                itemView.ivWarrantyItem.setImageResource(R.drawable.ic_image_holder)
            } else {
                Glide.with(itemView.context)
                    .load(warrantyItem.imageUrl)
                    .into(itemView.ivWarrantyItem)
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<WarrantyItem>() {
        override fun areItemsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListViewHolder {
        return SearchListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_warranty_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    override fun getItemCount(): Int {
        return searchList.size
    }
}