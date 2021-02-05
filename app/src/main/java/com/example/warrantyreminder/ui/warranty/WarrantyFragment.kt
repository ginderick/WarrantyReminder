package com.example.warrantyreminder.ui.warranty

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.FragmentWarrantyBinding
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.example.warrantyreminder.utils.Utils
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class WarrantyFragment : Fragment() {


    private var _binding: FragmentWarrantyBinding? = null
    private lateinit var itemId: String
    private lateinit var warrantyViewModel: WarrantyViewModel
    private val args: WarrantyFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWarrantyBinding.inflate(inflater, container, false)
        warrantyViewModel = ViewModelProvider(this).get(WarrantyViewModel::class.java)

        val warrantyItemId = args.warrantyItemId
        itemId = warrantyItemId


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch() {

            warrantyViewModel.apply {
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
        return when (item.itemId) {
            R.id.edit_settings -> {
                editWarrantyItem()
                 true
            }
            else -> {
                findNavController().navigate(R.id.action_warrantyFragment_to_navigation_home)
                 true
            }
        }
    }



    private fun editWarrantyItem() {

        val bundle = Bundle().apply {
            putString("operationType", "EDITING")
            putString("warrantyItemId", itemId)
        }
        findNavController().navigate(
            R.id.action_warrantyFragment_to_addEditWarrantyFragment,
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