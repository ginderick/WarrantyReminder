package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_warranty.*

class AddWarrantyFragment : Fragment() {

    var TAG: String = "lifecycle"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentEditWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val warrantyCollectionRef = Firebase.firestore.collection("warranty")
    private lateinit var warrantyItemId: String
    private lateinit var timestamp: Timestamp


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        setHasOptionsMenu(true)

        _binding = FragmentEditWarrantyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }


    private fun addWarrantyItem(warrantyItem: WarrantyItem) {

        when {
            textItemName.editText?.text.toString().isEmpty() -> {
                textItemName.error = "Enter name"
            }
            etItemDescription.editText?.text.toString().isEmpty() -> {
                etItemDescription.error = "Enter description"
                textItemName.error = null
            }
            else -> {
                homeViewModel.getDocumentReference().apply {
                    warrantyItemId = this.id
                    timestamp = Timestamp.now()
                    this.set(warrantyItem)
                }
                sendWarrantyItemBundle()
            }
        }
    }

    private fun getWarrantyItem(): WarrantyItem {
        return WarrantyItem(
            itemName = textItemName.editText?.text.toString(),
            itemDescription = etItemDescription.editText?.text.toString(),
            expirationDate = etExpiryDate.text.toString()
        )
    }

    //Send warrantyItem bundle to WarrantyFragment
    private fun sendWarrantyItemBundle() {

        val warrantyItem = WarrantyItem(
            id = warrantyItemId,
            itemName = textItemName.editText?.text.toString(),
            itemDescription = etItemDescription.editText?.text.toString(),
            expirationDate = etExpiryDate.text.toString()
        )

        val bundle = Bundle().apply {
            putSerializable(
                "warrantyItem", warrantyItem
            )
        }
        findNavController().navigate(
            R.id.action_addFragment_to_warrantyFragment,
            bundle
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addWarrantyItem(getWarrantyItem())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}