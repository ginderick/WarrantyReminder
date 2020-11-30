package com.example.warrantyreminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.warrantyreminder.model.WarrantyItem

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_warranty.view.*
import kotlinx.android.synthetic.main.fragment_warranty_item.view.*
import kotlinx.android.synthetic.main.fragment_warranty_item.view.tvExpiryDate
import kotlinx.android.synthetic.main.fragment_warranty_item.view.tvItemName


class HomeAdapter(options: FirestoreRecyclerOptions<WarrantyItem>) : FirestoreRecyclerAdapter<WarrantyItem, HomeAdapter.WarrantyItemViewHolder>(
    options
) {

    private var warrantyItemPosition: Int? = null

    //get Item position clicked
    fun getWarrantyItemPosition(): Int {
        return warrantyItemPosition!!
    }

    inner class WarrantyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarrantyItemViewHolder {
        return WarrantyItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_warranty_item,
                parent,
                false
            )
        )
    }



    private var onItemClickListener: ((WarrantyItem) -> Unit)? = null


    fun setOnItemClickListener(listener: (WarrantyItem) -> Unit) {
        onItemClickListener = listener
    }



    override fun onBindViewHolder(
        holder: WarrantyItemViewHolder,
        position: Int,
        model: WarrantyItem
    ) {
        holder.itemView.apply {
            warrantyItemPosition = position
            tvItemName.text = model.itemName
            tvExpiryDate.text = model.expirationDate

            setOnClickListener {
                onItemClickListener?.let { it(model) }
            }
        }
    }

    //    private val differCallback = object : DiffUtil.ItemCallback<WarrantyItem>() {
//        override fun areItemsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: WarrantyItem, newItem: WarrantyItem): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this, differCallback)


//    override fun getItemCount(): Int = differ.currentList.size




}


