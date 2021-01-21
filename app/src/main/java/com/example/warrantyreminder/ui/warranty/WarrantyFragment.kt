package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentWarrantyBinding
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.example.warrantyreminder.utils.Utils
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalCoroutinesApi
class WarrantyFragment : Fragment() {


    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentWarrantyBinding? = null
    private val args: WarrantyFragmentArgs by navArgs()
    private lateinit var itemId: String


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
        itemId = warrantyItemId

        viewLifecycleOwner.lifecycleScope.launch {


            homeViewModel.apply {
                getWarrantyItem(itemId)
                warrantyItem.observe(viewLifecycleOwner, Observer {
                    tvItemName.text = it.itemName
                    tvItemDescription.text = it.itemDescription
                    tvExpiryDate.text = Utils.convertMillisToString(it.expirationDate)


                    Glide.with(requireContext())
                        .load(it.imageUrl)
                        .into(ivWarranty)
                })
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", "onOptionsItemSelected called")
        when (item.itemId) {
            R.id.edit_settings -> {
                editWarrantyItem()
            }
            else -> findNavController().navigate(R.id.action_warrantyFragment_to_navigation_home)
        }

        return super.onOptionsItemSelected(item)
    }


    private fun editWarrantyItem() {

        val bundle = Bundle().apply {
            putString("warrantyItemId", itemId)
            putString("operationType", "EDITING")
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