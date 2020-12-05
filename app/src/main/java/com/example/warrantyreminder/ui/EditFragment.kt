package com.example.warrantyreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.register.EditViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_warranty.*

class EditFragment : Fragment() {

    var TAG: String = "lifecycle"

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = args.warrantyItem

        etItemName.setText(item.itemName)
        etItemDescription.setText(item.itemDescription)
        etExpiryDate.setText(item.expirationDate)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_item -> {
                updateWarrantyItem(args.warrantyItem.id)
                Toast.makeText(context, "Saved Item", Toast.LENGTH_LONG).show()
                val bundle = Bundle().apply {
                    putSerializable("warrantyItem", getWarrantyItemDetails())
                }
                findNavController().navigate(
                    R.id.action_editFragment_to_warrantyFragment,
                    bundle
                )
                true
            }
            else -> showCancelWarrantyEditDialog()
        }

    }

    private fun getWarrantyItemDetails(): WarrantyItem {

        Log.d(TAG, args.warrantyItem.id)
        return WarrantyItem(
            //return the id to WarrantyFragment
            id = args.warrantyItem.id,
            itemName = etItemName.text.toString(),
            itemDescription = etItemDescription.text.toString(),
            expirationDate = etExpiryDate.text.toString(),
        )
    }

    private fun isWarrantItemChanged(): Boolean {
        return args.warrantyItem.itemName != etItemName.text.toString() &&
                args.warrantyItem.itemDescription != etItemDescription.text.toString() &&
                args.warrantyItem.expirationDate != etExpiryDate.text.toString()
    }

    private fun updateWarrantyItem(warrantyItemId: String) {
        warrantyCollectionRef.document(warrantyItemId).update(
            mapOf(
                "itemName" to etItemName.text.toString(),
                "itemDescription" to etItemDescription.text.toString(),
                "expirationDate" to etExpiryDate.text.toString()
            )
        )
    }

    private fun showCancelWarrantyEditDialog(): Boolean {

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel")
            .setMessage("Are you sure you want to cancel edit? ")
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Yes") { dialog, which ->

                val bundle = Bundle().apply {
                    putSerializable("warrantyItem", getWarrantyItemDetails())
                }
                findNavController().navigate(
                    R.id.action_editFragment_to_warrantyFragment,
                    bundle
                )
            }
            .create()

        dialog.show()

        return true
    }


}