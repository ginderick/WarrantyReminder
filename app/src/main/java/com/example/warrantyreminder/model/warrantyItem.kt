package com.example.warrantyreminder.model

data class warrantyItem(
    val id: Int,
    val itemName: String,
    val itemDescription: String,
    val expirationDate: String,
    val isExpired: Boolean
)