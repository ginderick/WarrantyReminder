package com.example.warrantyreminder.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchViewModel : ViewModel() {

    private var firestoreRepository = FirestoreRepository()

    private val _warrantyItemsList = MutableLiveData<List<WarrantyItem>>()
    val warrantyItemList: LiveData<List<WarrantyItem>> = _warrantyItemsList



    fun queryList() = viewModelScope.launch {
        firestoreRepository.queryWarrantyList().collect {
            _warrantyItemsList.value = it
        }
    }

    fun queryWarrantyItem(searchText: String) = viewModelScope.launch {
        _warrantyItemsList.value = null
        firestoreRepository.queryWarrantyItem(searchText).collect {
            _warrantyItemsList.value = it
        }
    }
}