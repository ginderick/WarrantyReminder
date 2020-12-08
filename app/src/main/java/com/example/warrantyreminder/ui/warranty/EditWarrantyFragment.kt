package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.register.EditViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_warranty.*

class EditWarrantyFragment : Fragment() {

    var TAG: String = "lifecycle"

    private lateinit var editViewModel: EditViewModel
    private var _binding: FragmentEditWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: EditWarrantyFragmentArgs by navArgs()
    private val warrantyCollectionRef = Firebase.firestore.collection("warranty")
    private lateinit var warrantyItem: WarrantyItem


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
        warrantyItem = args.warrantyItem

        textItemName.editText?.setText(warrantyItem.itemName)
        etItemDescription.editText?.setText(warrantyItem.itemDescription)
        etExpiryDate.setText(warrantyItem.expirationDate)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    private fun sendWarrantyItemBundle() {
        val bundle = Bundle().apply {
            putSerializable("warrantyItem", getWarrantyItemDetails())
        }
        findNavController().navigate(
            R.id.action_editFragment_to_warrantyFragment,
            bundle
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_item -> {
                updateWarrantyItem()
                true
            }
            else -> showCancelWarrantyEditDialog()
        }

    }

    private fun getWarrantyItemDetails(): WarrantyItem {

        return WarrantyItem(
            id = args.warrantyItem.id,
            itemName = textItemName.editText?.text.toString(),
            itemDescription = etItemDescription.editText?.text.toString(),
            expirationDate = etExpiryDate.text.toString(),
        )
    }

    private fun updateWarrantyItem() {
        when {
            textItemName.editText?.text.toString().isEmpty() -> {
                textItemName.error = "Enter name"
            }
            etItemDescription.editText?.text.toString().isEmpty() -> {
                etItemDescription.error = "Enter description"
                textItemName.error = null
            }
            else -> {
                textItemName.error = null
                etItemDescription.error = null
                warrantyCollectionRef.document(warrantyItem.id).update(
                    mapOf(
                        "itemName" to textItemName.editText?.text.toString(),
                        "itemDescription" to etItemDescription.editText?.text.toString(),
                        "expirationDate" to etExpiryDate.text.toString(),
                        "imageUrl" to ""
                    )
                )
                sendWarrantyItemBundle()
                Toast.makeText(context, "Saved Item", Toast.LENGTH_LONG).show()
            }
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}