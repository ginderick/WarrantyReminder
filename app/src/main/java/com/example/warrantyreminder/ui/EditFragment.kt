package com.example.warrantyreminder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.warrantyreminder.databinding.FragmentEditWarrantyBinding
import com.example.warrantyreminder.ui.register.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.android.synthetic.main.fragment_warranty.*
import kotlinx.android.synthetic.main.fragment_warranty.tvExpiryDate
import kotlinx.android.synthetic.main.fragment_warranty.tvItemDescription
import kotlinx.android.synthetic.main.fragment_warranty.tvItemName

class EditFragment: Fragment() {

    private lateinit var editViewModel: EditViewModel
    private var _binding: FragmentEditWarrantyBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editViewModel =
            ViewModelProvider(this).get(EditViewModel::class.java)

        _binding = FragmentEditWarrantyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = args.warrantyItem

        etItemName.setText(item.itemName)

    }
}