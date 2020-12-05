package com.example.warrantyreminder.model


import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class WarrantyItem(

    @DocumentId
    val id: String = "",
    val itemName: String = "",
    val itemDescription: String = "",
    val expirationDate: String = "",
) : Serializable