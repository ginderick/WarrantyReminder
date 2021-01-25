package com.example.warrantyreminder.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.ui.warranty.WarrantyAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {


    private lateinit var searchViewModel: SearchViewModel
    private var searchList: List<WarrantyItem> = ArrayList()

    //    private val searchListAdapter = SearchListAdapter(searchList)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser!!.uid
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

        //Setup Recycler View
        setupRecyclerView()
        searchViewModel.queryList()
        searchViewModel.warrantyItemList.observe(viewLifecycleOwner, Observer {
            warrantyAdapter.differ.submitList(it)
        })


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


//        search_field.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(text: Editable?) {
//                val searchText: String = search_field.text.toString()
//                if (searchText.isEmpty()) {
//                    searchViewModel.queryList()
//                } else {
//                    searchViewModel.queryWarrantyItem(searchText)
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//        })
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
}