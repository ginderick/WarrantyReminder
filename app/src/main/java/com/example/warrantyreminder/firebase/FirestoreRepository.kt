package com.example.warrantyreminder.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.warrantyreminder.model.WarrantyItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject

import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class FirestoreRepository {

    val firestore = FirebaseFirestore.getInstance()

    init {

        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    fun saveItem(warrantyItem: WarrantyItem) = CoroutineScope(Dispatchers.IO).launch {
        firestore.collection("warranty")
            .add(warrantyItem)
    }


    fun deleteItem(warrantyItem: String): Task<Void> {
        return firestore.collection("warranty")
            .document(warrantyItem)
            .delete()
    }


    fun updateWarrantyItem(warrantyItemId : String): Task<DocumentSnapshot> {
        return firestore.collection("warranty")
            .document(warrantyItemId)
            .get()
    }
}