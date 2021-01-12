package com.example.warrantyreminder.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        homeViewModel.queryList()

        homeViewModel.warrantyItemList.observe(viewLifecycleOwner, Observer {
            homeAdapter.differ.submitList(it)
        })

        fab_add.setOnClickListener {
            val bundle = Bundle().apply {
                putString("operationType", "CREATING")
                putString("warrantyItemId", "")
            }
            findNavController().navigate(R.id.editFragment, bundle)
        }

        //swipe to delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val warrantyItem = homeAdapter.differ.currentList[position]
                val warrantyItemId = warrantyItem.id
                homeViewModel.deleteItem(warrantyItemId)

                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvHome)
        }


        homeAdapter.setOnItemClickListener {

            //send data to WarrantyFragment
            val bundle = Bundle().apply {
                putString("warrantyItemId", it.id)
                putString("operationTypeString", "EDITING")
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



    //TODO 2. Add image in model class
    //TODO 3. Fix Back button due to it navigates back to register activity
    //TODO 4. Expiration Date convert to Date Dialog
    //TODO 5. Add signup button
}