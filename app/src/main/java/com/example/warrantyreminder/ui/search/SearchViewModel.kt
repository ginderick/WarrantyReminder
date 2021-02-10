package com.example.warrantyreminder.ui.search

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyreminder.firebase.FirestoreRepository
import com.example.warrantyreminder.model.WarrantyItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchViewModel
    @ViewModelInject
    constructor(
        val firestoreRepository: FirestoreRepository
    ): ViewModel() {



    private val _warrantyItemsList = MutableLiveData<List<WarrantyItem>>()
    val warrantyItemList: LiveData<List<WarrantyItem>> = _warrantyItemsList

    private val _warrantyItemId = MutableLiveData<String>()
    val warrantyItemId: LiveData<String> = _warrantyItemId





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

    fun setWarrantyItemId(warrantyItemId: String) {

        //clear warrantyItemId
        _warrantyItemId.value = null
        _warrantyItemId.value = warrantyItemId
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("WarrantyReminderApp", "SearchViewModelCleared")
    }
}