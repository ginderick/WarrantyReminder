package com.example.warrantyreminder.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.warrantyreminder.model.WarrantyItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject

import com.google.firebase.ktx.Firebase

class FirestoreRepository {

    val firestore = FirebaseFirestore.getInstance()
    private val db: FirebaseFirestore = Firebase.firestore
    val items: MutableLiveData<ArrayList<WarrantyItem>> = MutableLiveData()

    init {

        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun saveItem(warrantyItem: WarrantyItem): Task<DocumentReference> {
        return firestore.collection("warranty")
            .add(warrantyItem)
    }

    fun deleteItem(warrantyItem: String): Task<Void> {
        return firestore.collection("warranty")
            .document(warrantyItem)
            .delete()
    }

}