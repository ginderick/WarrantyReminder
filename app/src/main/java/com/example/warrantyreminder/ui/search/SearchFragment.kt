package com.example.warrantyreminder.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warrantyreminder.R
import com.example.warrantyreminder.ui.warranty.WarrantyAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {


    private lateinit var searchViewModel: SearchViewModel
    lateinit var warrantyAdapter: WarrantyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        searchViewModel.queryList()
        searchViewModel.warrantyItemList.observe(viewLifecycleOwner, Observer {
            warrantyAdapter.differ.submitList(it)
        })

        setTextListener()

        warrantyAdapter.setOnItemClickListener {

            //send data to WarrantyFragment
            val bundle = Bundle().apply {
                putString("warrantyItemId", it.id)
                putString("operationTypeString", "EDITING")
            }
            findNavController().navigate(
                R.id.action_navigation_search_to_warrantyFragment,
                bundle
            )
        }
    }

    private fun setupRecyclerView() {
        warrantyAdapter = WarrantyAdapter()
        search_list.apply {
            adapter = warrantyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    private fun setTextListener() {
        search_field.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.queryWarrantyItem(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (search_field.query.isEmpty()) {
                    searchViewModel.queryList()
                } else {
                    searchViewModel.queryWarrantyItem(newText)
                }
                return false
            }
        })
    }
}