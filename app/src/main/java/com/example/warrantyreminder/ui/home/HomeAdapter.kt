package com.example.warrantyreminder.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_warranty_item.view.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.WarrantyItemViewHolder>() {

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
            tvExpiryDate.text = warrantyItem.expirationDate
            tvItemDescription.text = warrantyItem.itemDescription
            ivWarrantyItem.setImageResource(R.drawable.iphone)

//            Glide.with(context)
//                .load(warrantyItem.imageUrl)
//                .into(ivWarrantyItem)


            setOnClickListener {
                onItemClickListener?.let { it(warrantyItem) }
            }
        }
    }


}


