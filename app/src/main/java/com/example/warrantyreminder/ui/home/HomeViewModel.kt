package com.example.warrantyreminder.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class HomeViewModel : ViewModel() {


    private val _warrantyItemsList = MutableLiveData<List<WarrantyItem>>()
    val warrantyItemList: LiveData<List<WarrantyItem>> = _warrantyItemsList

    private val _warrantyItem = MutableLiveData<WarrantyItem>()
    val warrantyItem: LiveData<WarrantyItem> = _warrantyItem

    private val _warrantyItemId = MutableLiveData<String>()
    val warrantyItemId: LiveData<String> = _warrantyItemId




    var firestoreRepository = FirestoreRepository()


    fun queryList() = viewModelScope.launch {
        firestoreRepository.queryWarrantyList().collect {
            Log.d("WarrantyItem in VM", it.toString())
            _warrantyItemsList.value = it
        }
    }

    fun addItem() =
        firestoreRepository.addItem(_warrantyItemId.value!!)



    fun setWarrantyItem(warrantyItem: WarrantyItem) {
        firestoreRepository.setWarrantyItem(warrantyItem)
    }

    fun createDocument(): DocumentReference {
        val document = firestoreRepository.createDocument()
        _warrantyItemId.value = document.id
        return document
    }





    fun deleteItem(item: String) {
        firestoreRepository.deleteItem(item).addOnSuccessListener {
            Log.d("Firebase", "document deleted")
        }
            .addOnFailureListener {
                Log.d("Firebase", "Delete failed")
            }
    }

    fun getWarrantyItem(warrantyItemId: String) = viewModelScope.launch {
        firestoreRepository.getWarrantyItem(warrantyItemId).collect {
            _warrantyItem.value = it
        }
    }




}