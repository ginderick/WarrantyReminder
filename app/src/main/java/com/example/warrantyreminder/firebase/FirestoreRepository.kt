package com.example.warrantyreminder.firebase

import android.util.Log
import com.example.warrantyreminder.model.WarrantyItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FirestoreRepository {

    val firestore = FirebaseFirestore.getInstance()

    init {

        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun saveItem(warrantyItem: WarrantyItem) : Task<Void> {
        return firestore.collection("warranty")
            .document("warrantyItem")
            .set(warrantyItem)
    }

    fun deleteItem(warrantyItem: WarrantyItem) : Task<Void> {
        return firestore.collection("warranty")
            .document("warrantyItem")
            .delete()

    }


}