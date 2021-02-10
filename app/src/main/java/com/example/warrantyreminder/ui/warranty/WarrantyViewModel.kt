package com.example.warrantyreminder.ui.warranty

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyreminder.R
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.model.WarrantyPhoto
import com.example.warrantyreminder.utils.sendNotification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
class WarrantyViewModel
    @ViewModelInject
    constructor(
        val firestoreRepository: FirestoreRepository
    ): ViewModel() {

    private val _warrantyItemsList = MutableLiveData<List<WarrantyItem>>()
    val warrantyItemList: LiveData<List<WarrantyItem>> = _warrantyItemsList

    private val _warrantyItem = MutableLiveData<WarrantyItem>()
    val warrantyItem: LiveData<WarrantyItem> = _warrantyItem

    private val _warrantyItemId = MutableLiveData<String>()
    val warrantyItemId: LiveData<String> = _warrantyItemId


    private var itemName = ""
    private var itemDescription = ""
    private var expirationDate = 0L


    fun getWarrantyItem(warrantyItemId: String) = viewModelScope.launch {
        firestoreRepository.getWarrantyItem(warrantyItemId).collect {
            _warrantyItem.value = it

            populateWarrantyItem()
        }
    }

    private fun populateWarrantyItem() {
        itemName = _warrantyItem.value!!.itemName
        itemDescription = _warrantyItem.value!!.itemDescription
        expirationDate = _warrantyItem.value!!.expirationDate

    }

    fun updateWarrantyItem(warrantyItemId: String, itemName: String, itemDescription: String, expirationDate: Long) {
        firestoreRepository.updateWarrantyItem(warrantyItemId, itemName, itemDescription, expirationDate)
    }

    fun setWarrantyItemId(warrantyItemId: String) {
        _warrantyItemId.value = warrantyItemId
    }

    fun startNotification(context: Context) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification("This is a notification", context)
    }


}