package com.example.warrantyreminder.model

import java.io.Serializable

data class WarrantyItem(
    val itemName: String,
    val itemDescription: String,
    val expirationDate: String,
    val isExpired: Boolean = false

) : Serializable