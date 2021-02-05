package com.example.warrantyreminder.ui.warranty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.model.WarrantyPhoto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class WarrantyViewModel : ViewModel() {

    private val _warrantyItemsList = MutableLiveData<List<WarrantyItem>>()
    val warrantyItemList: LiveData<List<WarrantyItem>> = _warrantyItemsList

    private val _warrantyItem = MutableLiveData<WarrantyItem>()
    val warrantyItem: LiveData<WarrantyItem> = _warrantyItem

    private val _warrantyItemId = MutableLiveData<String>()
    val warrantyItemId: LiveData<String> = _warrantyItemId

    private var firestoreRepository = FirestoreRepository()


    fun getWarrantyItem(warrantyItemId: String) = viewModelScope.launch {
        firestoreRepository.getWarrantyItem(warrantyItemId).collect {
            _warrantyItem.value = it
        }
    }

    fun updateWarrantyItem(warrantyItemId: String, itemName: String, itemDescription: String, expirationDate: Long) {
        firestoreRepository.updateWarrantyItem(warrantyItemId, itemName, itemDescription, expirationDate)
    }

    fun setWarrantyItemId(warrantyItemId: String) {
        _warrantyItemId.value = warrantyItemId
    }
}