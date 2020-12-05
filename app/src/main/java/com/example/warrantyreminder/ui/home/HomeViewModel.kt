package com.example.warrantyreminder.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    var firestoreRepository = FirestoreRepository()

     fun saveItem(item: WarrantyItem) {
        firestoreRepository.saveItem(item)
    }

    fun deleteItem(item: String) {
        firestoreRepository.deleteItem(item).addOnSuccessListener {
            Log.d("Firebase", "document deleted")
        }
            .addOnFailureListener {
                Log.d("Firebase", "Delete failed")
            }
    }

    fun getDocumentReference() =firestoreRepository.getDocumentReference()

}