package com.example.warrantyreminder.ui.warranty

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


class WarrantyAdapter : RecyclerView.Adapter<WarrantyAdapter.WarrantyItemViewHolder>() {

    inner class WarrantyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onItemClickListener: ((WarrantyItem) -> Unit)? = null


    private val differCallback = object : DiffUtil.ItemCallback<WarrantyItem>() {
        override fun areItemsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarrantyItemViewHolder {
        return WarrantyItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_warranty_item,
                parent,
                false
            )
        )
    }

    fun setOnItemClickListener(listener: (WarrantyItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: WarrantyItemViewHolder, position: Int) {
        val warrantyItem = differ.currentList[position]
        holder.itemView.apply {

            tvItemName.text = warrantyItem.itemName
            tvItemDescription.text = warrantyItem.itemDescription
            tvExpiryDate.text = Utils.convertMillisToString(warrantyItem.expirationDate)

            if (warrantyItem.imageUrl.isEmpty()) {
                ivWarrantyItem.setImageResource(R.drawable.ic_image_holder)
            } else {
                Glide.with(context)
                    .load(warrantyItem.imageUrl)
                    .into(ivWarrantyItem)
            }


            setOnClickListener {
                onItemClickListener?.let { it(warrantyItem) }
            }
        }
    }


}


