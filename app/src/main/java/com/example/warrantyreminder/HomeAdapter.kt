package com.example.warrantyreminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.warrantyreminder.model.WarrantyItem

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_warranty_item.view.*


class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.WarrantyItemViewHolder>() {

    inner class WarrantyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    private val differCallback = object : DiffUtil.ItemCallback<WarrantyItem>() {
        override fun areItemsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun getItemCount(): Int = differ.currentList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarrantyItemViewHolder {
        return WarrantyItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_warranty_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WarrantyItemViewHolder, position: Int) {

        val warrantyItem = differ.currentList[position]
        holder.itemView.apply {
            tvItemName.text = warrantyItem.itemName
            tvExpiryDate.text = warrantyItem.expirationDate

            setOnClickListener {
                onItemClickListener?.let { it(warrantyItem) }
            }
        }
    }

    private var onItemClickListener: ((WarrantyItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (WarrantyItem) -> Unit) {
        onItemClickListener = listener
    }


}


