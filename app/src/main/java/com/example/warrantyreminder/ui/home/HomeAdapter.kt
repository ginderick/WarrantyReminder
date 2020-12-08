package com.example.warrantyreminder.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.fragment_warranty_item.view.*


class HomeAdapter(options: FirestoreRecyclerOptions<WarrantyItem>) : FirestoreRecyclerAdapter<WarrantyItem, HomeAdapter.WarrantyItemViewHolder>(
    options
) {


    private var onItemClickListener: ((WarrantyItem) -> Unit)? = null
    var warrantyItemPosition: Int? = null


    inner class WarrantyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {

        }

    }




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



    override fun onBindViewHolder(
        holder: WarrantyItemViewHolder,
        position: Int,
        model: WarrantyItem
    ) {
        holder.itemView.apply {
            warrantyItemPosition = position
            tvItemName.text = model.itemName
            tvExpiryDate.text = model.expirationDate
            tvItemDescription.text = model.itemDescription

            Glide.with(context)
                .load(model.imageUrl)
                .into(ivWarrantyItem)


            setOnClickListener {
                onItemClickListener?.let { it(model) }
            }

        }

    }


}


