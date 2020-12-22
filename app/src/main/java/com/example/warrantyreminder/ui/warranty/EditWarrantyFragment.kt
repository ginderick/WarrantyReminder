package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class EditWarrantyFragment : Fragment() {

    var TAG: String = "lifecycle"

    private var _binding: FragmentEditWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: EditWarrantyFragmentArgs by navArgs()
    private val warrantyCollectionRef = Firebase.firestore.collection("users")
    private val user = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var homeViewModel: HomeViewModel
    lateinit var itemId: String


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val warrantyItemId = args.warrantyItemId
        itemId = warrantyItemId
        Log.d("EditWarranty", itemId)

        homeViewModel.apply {
            getWarrantyItem(itemId)
            warrantyItem.observe(viewLifecycleOwner, Observer {
                textItemName.editText?.setText(it.itemName)
                etItemDescription.editText?.setText(it.itemDescription)
                etExpiryDate.setText(it.expirationDate)
            })
        }


        fab_edit.setOnClickListener {
            warrantyCollectionRef.document(user!!).collection("warranty").document(itemId).update(
                mapOf(
                    "itemName" to textItemName.editText?.text.toString(),
                    "itemDescription" to etItemDescription.editText?.text.toString(),
                    "expirationDate" to etExpiryDate.text.toString(),
                    "imageUrl" to ""
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
        Log.d("EditWarranty", "onCreateOptionsMenuCalled")
    }

    private fun sendWarrantyItemBundle() {

        Log.d("EditWarranty", "Send bundle $itemId")
        val bundle = Bundle().apply {
            putString("warrantyItemId", itemId)
        }
        findNavController().navigate(
            R.id.action_editFragment_to_warrantyFragment,
            bundle
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("EditWarranty", "onOptionsItemSelectedCalled")
        return when (item.itemId) {
            R.id.save_item -> {
                updateWarrantyItem()
                true
            }
            else -> showCancelWarrantyEditDialog()
        }
    }

    private fun getWarrantyItemDetails(): WarrantyItem {
        Log.d("EditWarranty", "getWarrantyItemDetailsCalled")

        return WarrantyItem(
            id = args.warrantyItemId!!,
            itemName = textItemName.editText?.text.toString(),
            itemDescription = etItemDescription.editText?.text.toString(),
            expirationDate = etExpiryDate.text.toString(),
        )
    }

    private fun updateWarrantyItem() {
        Log.d("EditWarranty", "updateWarrantyItemCalled")
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
                Log.d("EditWarrantyFragment", itemId)
                warrantyCollectionRef.document(user!!).collection("warranty").document(itemId)
                    .update(
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

        Log.d("EditWarranty", "showCancelWarrantyEditDialogCalled")


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