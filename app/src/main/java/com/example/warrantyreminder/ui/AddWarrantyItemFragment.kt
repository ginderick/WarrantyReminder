package com.example.warrantyreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentAddWarrantyBinding
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.example.warrantyreminder.ui.register.EditViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddWarrantyItemFragment : Fragment() {

    var TAG: String = "lifecycle"

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentAddWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val warrantyCollectionRef = Firebase.firestore.collection("warranty")
    private lateinit var warrantyItemId: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        setHasOptionsMenu(true)

        _binding = FragmentAddWarrantyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

//    private fun addWarrantyItem() {
//        warrantyCollectionRef.document().set(
//            WarrantyItem(
//                itemName = etItemName.text.toString(),
//                itemDescription = etItemDescription.text.toString(),
//                expirationDate = etExpiryDate.text.toString(),
//            )
//        ).addOnSuccessListener { it ->
//            warrantyItemId = "here"
//        }
//
//        Log.d(TAG, warrantyItemId)
//        Toast.makeText(context, "add warrantyItem $warrantyItemId", Toast.LENGTH_LONG).show()
//
//
//    }

    private fun addWarrantyItem(warrantyItem: WarrantyItem) {
        homeViewModel.getDocumentReference().apply {
            warrantyItemId = this.id
            this.set(warrantyItem)
        }
    }

    private fun getWarrantyItem(): WarrantyItem {
        return WarrantyItem(
            itemName = etItemName.text.toString(),
            itemDescription = etItemDescription.text.toString(),
            expirationDate = etExpiryDate.text.toString()
        )
    }

    //function to send warrantyItem bundle to WarrantyFragment
    private fun getWarrantyItemBundle(): WarrantyItem {
        return WarrantyItem(
            id = warrantyItemId,
            itemName = etItemName.text.toString(),
            itemDescription = etItemDescription.text.toString(),
            expirationDate = etExpiryDate.text.toString()
        )
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addWarrantyItem(getWarrantyItem())
                val bundle = Bundle().apply {
                    putSerializable(
                        "warrantyItem", getWarrantyItemBundle()
                    )
                }
                findNavController().navigate(
                    R.id.action_addFragment_to_warrantyFragment,
                    bundle
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }


}