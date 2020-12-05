package com.example.warrantyreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.coroutines.launch

class WarrantyFragment : Fragment() {


    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentWarrantyBinding? = null
    var TAG: String = "lifecycle"
    private val args: WarrantyFragmentArgs by navArgs()
    private val warrantyItemFromEditFragment = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWarrantyBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val warrantyItem = args.warrantyItem

        tvItemName.text = warrantyItem.itemName
        tvItemDescription.text = warrantyItem.itemDescription
        tvExpiryDate.text = warrantyItem.expirationDate


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.edit_settings -> {
                editWarrantyItem()
            }
            else -> findNavController().navigate(R.id.action_warrantyFragment_to_navigation_home)
        }

        return super.onOptionsItemSelected(item)
    }


    private fun editWarrantyItem() {

            //args is from HomeFragment
            val bundle = Bundle().apply {
                putSerializable("warrantyItem", args.warrantyItem)
            }
            findNavController().navigate(
                R.id.action_warrantyFragment_to_editFragment,
                bundle
            )
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}