package com.example.warrantyreminder.ui.warranty

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.model.WarrantyPhoto
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.coroutines.*


private const val REQUEST_CODE_IMAGE_PICK = 0

@ExperimentalCoroutinesApi
class EditWarrantyFragment : Fragment() {

    private var _binding: FragmentEditWarrantyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: EditWarrantyFragmentArgs by navArgs()
    private val warrantyCollectionRef = Firebase.firestore.collection("users")
    private val user = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var homeViewModel: HomeViewModel
    lateinit var itemId: String
    lateinit var operationType: String
    private var curFile: Uri? = null
    private val imageRef = Firebase.storage.reference
    private lateinit var photo: WarrantyPhoto


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize objects from the bundle
        operationType = args.operationType
        itemId = args.warrantyItemId

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

        when (operationType) {
            "CREATING" -> {
            }
            "EDITING" -> {

                homeViewModel.apply {
                    getWarrantyItem(itemId)
                    warrantyItem.observe(viewLifecycleOwner, Observer {
                        textItemName.editText?.setText(it.itemName)
                        etItemDescription.editText?.setText(it.itemDescription)
                        etExpiryDate.setText(it.expirationDate)
                    })

                }

                warrantyImage.setOnClickListener {
                    Intent(Intent.ACTION_GET_CONTENT).also {
                        it.type = "image/*"
                        startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
                    }
                }

                btnUploadImage.setOnClickListener {
                    uploadImageToStorage(itemId)
                }

                btnDownloadImage.setOnClickListener {
                    downloadImage()
                }


            }
        }
    }

    private fun uploadImageToStorage(warrantyItemId: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            curFile?.let {
                val imageRef = imageRef.child("images/$warrantyItemId/${it.lastPathSegment}")
                val uploadTask = imageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    val downloadUrl = imageRef.downloadUrl
                    downloadUrl.addOnSuccessListener { uri ->
                        photo = WarrantyPhoto(remoteUri = uri.toString())
                        homeViewModel.updatePhotoDb(itemId, photo)
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Successfully uploaded image", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun downloadImage() {
        val uri = "https://firebasestorage.googleapis.com/v0/b/androidprojects-280512.appspot.com/o/images%2F0FQxrztNps4hGBnbPrYd%2Fimage%3A106?alt=media&token=b0897649-c4f0-45dc-88b2-f4d8af1c6fee"
        Glide.with(requireContext())
            .load(uri)
            .into(warrantyDownloadImage)
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                curFile = it
                warrantyImage.setImageURI(it)
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
        navigateToWarrantyFragment(bundle)
    }

    private fun navigateToWarrantyFragment(bundle: Bundle) {
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
            else -> when (operationType) {
                "CREATING" -> super.onOptionsItemSelected(item)
                else -> {
                    showCancelWarrantyEditDialog()
                    return true
                }
            }
        }
    }

    private fun addWarrantyItem() {

        val textItemName = textItemName.editText?.text.toString()
        val itemDescription = etItemDescription.editText?.text.toString()


        //Check if editTexts are empty
        when {
            textItemName.isEmpty() -> {
                etItemNameText.error = "Enter name"
            }
            itemDescription.isEmpty() -> {
                etItemDescription.error = "Enter description"
                etItemNameText.error = null
            }

            else -> {

                //If all editTexts are satisfied, create a warranty item object
                val warrantyItem = createWarrantyItem()

                homeViewModel.apply {
                    saveWarrantyItem(warrantyItem)
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
            }
        }
    }

    private fun showCancelWarrantyEditDialog() {

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel")
            .setMessage("Are you sure you want to cancel edit? ")
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Yes") { dialog, which ->
                sendWarrantyItemBundle()
            }
            .create()
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}