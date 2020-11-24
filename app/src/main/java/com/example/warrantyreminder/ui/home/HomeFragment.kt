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
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter

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
        homeAdapter.differ.submitList(warrantyList)

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

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()

        rvHome.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    private val warrantyList: List<WarrantyItem> = listOf(
        WarrantyItem(
            id = 1,
            itemName = "Phone",
            itemDescription = "Mobile Phone",
            expirationDate = "12/12/2020",
        ),
        WarrantyItem(
            id = 2,
            itemName = "PS5",
            itemDescription = "Play",
            expirationDate = "12/13/2020",
        ),
        WarrantyItem(
            id = 3,
            itemName = "PS5123",
            itemDescription = "Pla123y",
            expirationDate = "12/13/2020",
        )
    )



    //TODO 1. Finish home recycler view, should display placeholders - OK
    //TODO 2. Clickable recyclerView items = OK
    //TODO 3. Create viewmodel for Warranty Fragment - OK



}