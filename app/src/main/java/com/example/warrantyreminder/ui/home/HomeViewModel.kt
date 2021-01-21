package com.example.warrantyreminder.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.model.WarrantyPhoto
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

    private var firestoreRepository = FirestoreRepository()


    fun queryList() = viewModelScope.launch {
        firestoreRepository.queryWarrantyList().collect {
            _warrantyItemsList.value = it
        }
    }

    fun saveWarrantyItem(warrantyItem: WarrantyItem) {
        val document = firestoreRepository.createWarrantyItem()
        _warrantyItemId.value = document.id
        firestoreRepository.addWarrantyItem(warrantyItem, _warrantyItemId.value!!)
    }


    fun deleteItem(item: String) {
        firestoreRepository.deleteItem(item)
    }



    fun getWarrantyItem(warrantyItemId: String) = viewModelScope.launch {
        firestoreRepository.getWarrantyItem(warrantyItemId).collect {
            _warrantyItem.value = it
        }
    }

    fun addPhoto(warrantyItemId: String) {
        firestoreRepository.addPhoto(warrantyItemId)
    }

    fun updateWarrantyItem(warrantyItemId: String, itemName: String, itemDescription: String, expirationDate: Long) {
        firestoreRepository.updateWarrantyItem(warrantyItemId, itemName, itemDescription, expirationDate)
    }


    fun updatePhotoDb(warrantyItemId: String, warrantyPhoto: WarrantyPhoto) {
        firestoreRepository.updatePhotoDb(warrantyItemId, warrantyPhoto)
    }




}