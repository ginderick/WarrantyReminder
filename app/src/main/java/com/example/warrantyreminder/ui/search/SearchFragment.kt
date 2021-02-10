package com.example.warrantyreminder.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warrantyreminder.R
import com.example.warrantyreminder.ui.home.HomeViewModel
import com.example.warrantyreminder.ui.warranty.WarrantyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment() {


    private lateinit var homeViewModel: HomeViewModel
    lateinit var warrantyAdapter: WarrantyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        Log.d("WarrantyReminderApp", "SearchViewModelCreated")
        setupRecyclerView()
        homeViewModel.apply {
            queryList()
            warrantyItemList.observe(viewLifecycleOwner, Observer {
                warrantyAdapter.differ.submitList(it)
            })
        }


        warrantyAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_warrantyFragment, createWarrantyItemBundle(it.id)
            )
        }

        setTextListener()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    private fun setupRecyclerView() {
        warrantyAdapter = WarrantyAdapter()
        search_list.apply {
            adapter = warrantyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun createWarrantyItemBundle(
        warrantyItemId: String
    ): Bundle {
        return Bundle().apply {
            putString("warrantyItemId", warrantyItemId)
        }
    }

    private fun setTextListener() {
        search_field.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                homeViewModel.queryWarrantyItem(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (search_field.query.isEmpty()) {
                    homeViewModel.queryList()
                } else {
                    homeViewModel.queryWarrantyItem(newText)
                }
                return false
            }
        })
    }
}