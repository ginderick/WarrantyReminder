package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AddWarrantyFragment : Fragment() {

    var TAG: String = "lifecycle"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentEditWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var warrantyItemIds: String


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


    private fun addWarrantyItem() {
        when {
            textItemName.editText?.text.toString().isEmpty() -> {
                textItemName.error = "Enter name"
            }
            etItemDescription.editText?.text.toString().isEmpty() -> {
                etItemDescription.error = "Enter description"
                textItemName.error = null
            }
            else -> {

                homeViewModel.apply {
                    setWarrantyItem()
                    createDocument()
                    addItem()
                }
                warrantyItemIds = homeViewModel.warrantyItemId.value!!
                sendWarrantyItemBundle()
            }
        }
    }

    private fun setWarrantyItem() {
         val warrantyItem =  WarrantyItem(
            itemName = textItemName.editText?.text.toString(),
            itemDescription = etItemDescription.editText?.text.toString(),
            expirationDate = etExpiryDate.text.toString(),
            imageUrl = ""
        )

        homeViewModel.setWarrantyItem(warrantyItem)
    }

    //Send warrantyItem bundle to WarrantyFragment
    private fun sendWarrantyItemBundle() {

        Log.d(TAG, "AddWarranty $warrantyItemIds")
        val bundle = Bundle().apply {
            putString(
                "warrantyItemId", warrantyItemIds
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
                addWarrantyItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}