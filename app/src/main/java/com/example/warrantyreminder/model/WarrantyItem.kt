package com.example.warrantyreminder.model


import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable

data class WarrantyItem(
    @DocumentId
    val id: String = "",
    val itemName: String = "",
    val itemDescription: String = "",
    val expirationDate: String = "",
    @Exclude
    val imageUrl: String = "",
    @ServerTimestamp
    var timeStamp: Timestamp? = null
) : Serializable