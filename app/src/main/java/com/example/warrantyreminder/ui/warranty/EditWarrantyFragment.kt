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

    private var _binding: FragmentEditWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: EditWarrantyFragmentArgs by navArgs()
    private val warrantyCollectionRef = Firebase.firestore.collection("users")
    private val user = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var homeViewModel: HomeViewModel
    lateinit var itemId: String
    lateinit var operationType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val operationTypeString = args.operationType
        operationType = operationTypeString!!
    }

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

        itemId = warrantyItemId!!

        if (operationType == "CREATING") {

        } else if (operationType == "EDITING") {

            homeViewModel.apply {
                getWarrantyItem(itemId)
                warrantyItem.observe(viewLifecycleOwner, Observer {
                    textItemName.editText?.setText(it.itemName)
                    etItemDescription.editText?.setText(it.itemDescription)
                    etExpiryDate.setText(it.expirationDate)
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    private fun sendWarrantyItemBundle() {

        val bundle = Bundle().apply {
            putString("warrantyItemId", itemId)
        }
        findNavController().navigate(
            R.id.action_editFragment_to_warrantyFragment,
            bundle
        )
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.save_item -> {
                if (operationType == "CREATING") {
                    addWarrantyItem()
                } else {
                    updateWarrantyItem()
                }
                true
            }
            else -> when(operationType) {
                "CREATING" -> super.onOptionsItemSelected(item)
                else -> showCancelWarrantyEditDialog()
            }
        }
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

                val warrantyItem = createWarrantyItem()
                homeViewModel.apply {
                    setWarrantyItem(warrantyItem)
                    createDocument()
                    addItem()
                }
                itemId = homeViewModel.warrantyItemId.value!!
                sendWarrantyItemBundle()
            }
        }
    }

    private fun createWarrantyItem(): WarrantyItem {
        return WarrantyItem(
            itemName = textItemName.editText?.text.toString(),
            itemDescription = etItemDescription.editText?.text.toString(),
            expirationDate = etExpiryDate.text.toString(),
            imageUrl = ""
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

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel")
            .setMessage("Are you sure you want to cancel edit? ")
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Yes") { dialog, which ->

                val bundle = Bundle().apply {
                    putString("warrantyItemId", itemId)
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