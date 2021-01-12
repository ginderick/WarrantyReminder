package com.example.warrantyreminder.model

import com.google.firebase.firestore.DocumentId
import java.util.*


data class WarrantyPhoto (
    @DocumentId
    val id : String = "",
    var remoteUri: String = "",
    var description : String = "",
    var dateTaken : Date = Date(),
    ){
}