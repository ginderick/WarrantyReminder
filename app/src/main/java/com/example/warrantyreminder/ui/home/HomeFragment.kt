package com.example.warrantyreminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warrantyreminder.HomeAdapter
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter
    private val warrantyItemRef = FirebaseFirestore.getInstance()
    private val query  = warrantyItemRef.collection("warranty")
    private val options = FirestoreRecyclerOptions.Builder<WarrantyItem>().setQuery(query, WarrantyItem::class.java).build()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        getAllCollection()

        test_button.setOnClickListener {
        }



        homeAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("Item", it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_warrantyFragment,
                bundle
            )

        }


    }

    private fun getAllCollection() {
        homeViewModel.getAllItems().observe(viewLifecycleOwner, Observer {
            homeAdapter.differ.submitList(it.toList())
        })
    }


    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()

        rvHome.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    //TODO 1. delete item in firestore
    //TODO 2. update item in firestore
    //TODO 3. Add UI in WarrantyFragment



}