package com.example.warrantyreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
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
    private var warrantyItemId = ""
    var TAG: String = "lifecycle"


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val item = args.warrantyItem
        Log.d(TAG, "Current warrantyItem id is $warrantyItemId in WarrantyFragment")
        warrantyItemId = args.warrantyItemId
        Log.d(TAG, "Now the warrantyItem id is $warrantyItemId in WarrantyFragment")

        //get WarrantyItem in viewmodel
        homeViewModel.updateWarrantyItem(warrantyItemId).addOnSuccessListener {it ->
            val warrantyItem = it.toObject<WarrantyItem>()

            tvItemName.text = warrantyItem?.itemName
            tvItemDescription.text = warrantyItem?.itemDescription
            tvExpiryDate.text = warrantyItem?.expirationDate
        }

        Log.d(TAG, "Reset the warrantyItem id is $warrantyItemId in WarrantyFragment")


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

//            else -> {
//                findNavController().navigate(
//                    R.id.action_warrantyFragment_to_navigation_home
//                )
//                true
//            }
        }
    }


    private fun editWarrantyItem() {

        //send data to EditFragment
        val bundle = Bundle().apply {
            putSerializable("warrantyItem", args.warrantyItem)
            putString("warrantyItemId", warrantyItemId)
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