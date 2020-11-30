package com.example.warrantyreminder.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem

class EditViewModel : ViewModel() {

    private var firestoreRepository = FirestoreRepository()

    fun saveItem(item: WarrantyItem) {
        firestoreRepository.saveItem(item).addOnSuccessListener {
            Log.d("Firebase", "document save")
        }
            .addOnFailureListener {
                Log.d("Firebase", "Save failed")
            }
    }

    fun deleteItem(item: String) {
        firestoreRepository.deleteItem(item).addOnSuccessListener {
            Log.d("Firebase", "document deleted")
        }
            .addOnFailureListener {
                Log.d("Firebase", "Delete failed")
            }
    }
}