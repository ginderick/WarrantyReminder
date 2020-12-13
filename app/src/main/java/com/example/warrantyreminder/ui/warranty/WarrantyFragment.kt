package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentWarrantyBinding
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.coroutines.launch

class WarrantyFragment : Fragment() {


    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentWarrantyBinding? = null
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

        val warrantyItemId = args.warrantyItemId


        lifecycleScope.launch {
            homeViewModel.getWarrantyItem(warrantyItemId!!).addSnapshotListener { value, error ->

                val item = value?.data
                Log.d("warrantyItem", item.toString())

                tvItemName.text = item?.get("itemName").toString()
                tvItemDescription.text = item?.get("itemDescription").toString()
                tvExpiryDate.text = item?.get("expirationDate").toString()
            }



        }
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
            putString("warrantyItem", args.warrantyItemId)
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