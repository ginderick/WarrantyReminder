package com.example.warrantyreminder.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentWarrantyBinding
import com.example.warrantyreminder.databinding.FragmentWarrantyItemBinding
import com.example.warrantyreminder.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_warranty.*

class WarrantyFragment : Fragment() {


    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentWarrantyBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: WarrantyFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentWarrantyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = args.warrantyItem

        tvItemName.text = item.itemName
        tvItemDescription.text = item.itemDescription
        tvExpiryDate.text = item.expirationDate
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle the up button here
        return when (item.itemId) {
            R.id.edit_settings -> {
                editWarrantyItem()
                true
            }
            else -> NavigationUI.onNavDestinationSelected(
                item, requireView().findNavController()
            ) || super.onOptionsItemSelected(item)
        }
    }


    private fun editWarrantyItem() {
        //send data to EditFragment
        val bundle = Bundle().apply {
            putSerializable("warrantyItem",args.warrantyItem)
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