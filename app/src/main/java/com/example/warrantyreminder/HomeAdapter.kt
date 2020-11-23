package com.example.warrantyreminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.warrantyreminder.model.warrantyItem

class HomeAdapter : RecyclerView.Adapter<WarrantyItemViewHolder>() {
    var data = listOf<warrantyItem>()

    override fun getItemCount(): Int = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarrantyItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.fragment_warranty_item, parent, false)
        return WarrantyItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: WarrantyItemViewHolder, position: Int) {

        val item = data[position]
        val res = holder.itemView.context.resources
    }
}

class WarrantyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}
