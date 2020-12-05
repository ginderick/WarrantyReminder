package com.example.warrantyreminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warrantyreminder.R
import com.example.warrantyreminder.model.WarrantyItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), HomeAdapter.RecyclerViewClickListener {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter
    private val warrantyItemRef = FirebaseFirestore.getInstance()
    private var warrantyItemId = ""
    var TAG: String = "lifecycle"

    override fun onStart() {
        super.onStart()
        homeAdapter.startListening()
    }


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

        add_button.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
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
                val document = homeAdapter.snapshots.getSnapshot(position).toObject<WarrantyItem>()
                val documentId = homeAdapter.snapshots.getSnapshot(position).id

                warrantyItemId = documentId
                homeViewModel.deleteItem(documentId)

                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        homeViewModel.saveItem(document!!)
                    }
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
                putSerializable("warrantyItem", it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_warrantyFragment,
                bundle
            )
        }



    }

    override fun onStop() {
        super.onStop()
        homeAdapter.stopListening()
    }

    private fun setupRecyclerView() {
        val query = warrantyItemRef.collection("warranty")
        val options = FirestoreRecyclerOptions.Builder<WarrantyItem>()
            .setQuery(query, WarrantyItem::class.java)
            .build()

        homeAdapter = HomeAdapter(options)


        rvHome.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun recyclerViewClick(v: View, position: Int) {

    }

    //TODO 2. Add AddWarrantyItemFragment for adding items - OK
    //TODO 3. Polish recyclerView UI
    //TODO 4. Recycler View arrange by created date via firestore
    //TODO 5. Add image in model class
}