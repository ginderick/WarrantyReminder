package com.example.warrantyreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.register.EditViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.android.synthetic.main.fragment_warranty.tvExpiryDate
import kotlinx.android.synthetic.main.fragment_warranty.tvItemDescription
import kotlinx.android.synthetic.main.fragment_warranty.tvItemName

class EditFragment: Fragment() {

    private lateinit var editViewModel: EditViewModel
    private var _binding: FragmentEditWarrantyBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: EditFragmentArgs by navArgs()
    private val warrantyCollectionRef = Firebase.firestore.collection("warranty")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editViewModel =
            ViewModelProvider(this).get(EditViewModel::class.java)
        setHasOptionsMenu(true)

        _binding = FragmentEditWarrantyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = args.warrantyItem


        etItemName.setText(item?.itemName)
        etItemDescription.setText(item?.itemDescription)
        etExpiryDate.setText(item?.expirationDate)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_item-> {
                updateWarrantyItem(args.warrantyItem.id)
                Toast.makeText(context, "Saved Item", Toast.LENGTH_LONG).show()
//                NavigationUI.onNavDestinationSelected(
//                    item, requireView().findNavController()
//                ) || super.onOptionsItemSelected(item)
//
                val bundle = Bundle().apply {
                    putSerializable("warrantyItem", getWarrantyItemDetails())
                }
                findNavController().navigate(
                    R.id.action_editFragment_to_warrantyFragment,
                    bundle
                )
                true
            }
            else -> NavigationUI.onNavDestinationSelected(
                item, requireView().findNavController()
            ) || super.onOptionsItemSelected(item)
        }
    }

    private fun getWarrantyItemDetails(): WarrantyItem {
        return WarrantyItem(
            itemName = etItemName.text.toString(),
            itemDescription =  etItemDescription.text.toString(),
            expirationDate = etExpiryDate.text.toString()
        )
    }

    private fun updateWarrantyItem(warrantyItemId: String) {

        Log.d("warrantyItemId", warrantyItemId)
        warrantyCollectionRef.document(warrantyItemId).update(
            mapOf(
                "itemName" to etItemName.text.toString(),
                "itemDescription" to etItemDescription.text.toString(),
                "expirationDate" to etExpiryDate.text.toString(),
                "isExpired" to false
            )
        )


    }


}