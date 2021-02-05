package com.example.warrantyreminder.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warrantyreminder.R
import com.example.warrantyreminder.ui.warranty.WarrantyAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    lateinit var warrantyAdapter: WarrantyAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.apply {
            queryList()
            warrantyItemList.observe(viewLifecycleOwner, Observer {
                warrantyAdapter.differ.submitList(it)
            })
        }

        fab_add.setOnClickListener {
            findNavController().navigate(
                R.id.action_warrantyFragment_to_navigation_home,
                createWarrantyItemBundle("CREATING")
            )
        }

        swipeToDelete()

        warrantyAdapter.setOnItemClickListener {
            val warrantyItemId = it.id
            findNavController().navigate(
                R.id.action_navigation_home_to_warrantyFragment,
                createWarrantyItemBundle("EDITING", warrantyItemId = warrantyItemId)
            )
        }
    }

    private fun createWarrantyItemBundle(
        operationType: String,
        warrantyItemId: String = ""
    ): Bundle {
        return Bundle().apply {
            putString("operationType", operationType)
            putString("warrantyItemId", warrantyItemId)
        }
    }

    private fun setupRecyclerView() {
        warrantyAdapter = WarrantyAdapter()
        rvHome.apply {
            adapter = warrantyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun swipeToDelete() {
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
                val warrantyItem = warrantyAdapter.differ.currentList[position]
                val warrantyItemId = warrantyItem.id

                homeViewModel.deleteItem(warrantyItemId)

                Snackbar.make(view!!, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvHome)
        }
    }




    //TODO 4. Set to ViewModel to retrieve warrantyItemId in all fragments
    //This is to utilize the viewModel instead of using bundle/argumnets, also to utilize popUpBackStack()
    //TODO 5. Add signup button
    //TODO 6. Add spinner (loading)
    //TODO 7. Add Notifications
}