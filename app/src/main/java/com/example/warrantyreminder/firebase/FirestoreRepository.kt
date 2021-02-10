package com.example.warrantyreminder.firebase

import android.util.Log

import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.model.WarrantyPhoto
import com.example.warrantyreminder.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FirestoreRepository
    @Inject
    constructor(
    firestore: FirebaseFirestore,
    firebaseAuth: FirebaseAuth
    ) {

    private val user = firebaseAuth.currentUser!!.uid
    private val imageRef = Firebase.storage.reference
    private val warrantyCollection = firestore.collection("users")
        .document(user).collection("warranty")


    fun addWarrantyItem(warrantyItem: WarrantyItem, warrantyItemId: String) {
        warrantyCollection.document(warrantyItemId).set(warrantyItem)
    }

    fun createWarrantyItem(): DocumentReference {
        return warrantyCollection.document()
    }

    fun addPhoto(warrantyItemId: String) {
        warrantyCollection.document(warrantyItemId)
            .collection("photos").document()
    }

    fun updatePhotoDb(warrantyItemId: String, warrantyPhoto: WarrantyPhoto) {

        val photoCollection = warrantyCollection.document(warrantyItemId).collection("photos")
        photoCollection.get().addOnSuccessListener {
            if (it.isEmpty) {
                val document = photoCollection.document()
                val currentPhotoId = document.id
                photoCollection.document(currentPhotoId).set(warrantyPhoto)
            }

            //update remoteUri of the photo
            photoCollection.get().addOnCompleteListener {
                val id = it.result!!.documents[0].id
                photoCollection.document(id).update(
                    mapOf(
                        "remoteUri" to warrantyPhoto.remoteUri,
                        "dateTaken" to Timestamp.now()
                    )
                )

                warrantyCollection.document(warrantyItemId).update(
                    mapOf(
                        "imageUrl" to warrantyPhoto.remoteUri
                    )
                )
            }
        }
    }

    fun deleteItem(warrantyItemId: String): Task<Void> {
        warrantyCollection.document(warrantyItemId).collection("photos").get().addOnCompleteListener {

            if (it.result!!.size() > 0) {
                //get the id of the document on the sub-collection photo
                val id = it.result!!.documents[0].id
                warrantyCollection.document(warrantyItemId).collection("photos").document(id).delete()
                imageRef.child("images/$user/$warrantyItemId/image.jpg").delete()
            }
        }
        return warrantyCollection.document(warrantyItemId).delete()
    }

    fun getWarrantyItem(warrantyItemId: String): Flow<WarrantyItem> {
        return callbackFlow {
            val listener =
                warrantyCollection.document(warrantyItemId).addSnapshotListener { value, _ ->
                    val warrantyItem = value?.toObject<WarrantyItem>()!!
                    offer(warrantyItem)
                }
            awaitClose {
                listener.remove()
            }
        }
    }



    fun updateWarrantyItem(
        warrantyItemId: String,
        itemName: String,
        itemDescription: String,
        expirationDate: Long
    ) {
        warrantyCollection.document(warrantyItemId)
            .update(
                mapOf(
                    "itemName" to itemName,
                    "itemDescription" to itemDescription,
                    "expirationDate" to expirationDate
                )
            )
    }

    fun queryWarrantyList(): Flow<List<WarrantyItem>> {
        return callbackFlow {
            val listener = warrantyCollection.orderBy(
                "timeStamp",
                Query.Direction.DESCENDING
            ).addSnapshotListener { value, error ->

                if (error != null) {
                    cancel(message = "Error fetching items", cause = error)
                    return@addSnapshotListener
                }

                val map = value?.documents!!.mapNotNull { it.toObject(WarrantyItem::class.java) }
                offer(map)
            }
            awaitClose {
                listener.remove()
            }
        }
    }

    fun queryWarrantyItem(textString: String): Flow<List<WarrantyItem>> {
        return callbackFlow {
            val listener = warrantyCollection.whereEqualTo("itemName", textString)
                .orderBy(
                "itemName",
                Query.Direction.DESCENDING
            ).addSnapshotListener { value, error ->
                Log.d("FirestoreRepository", value?.documents.toString())

                if (error != null) {
                    cancel(message = "Error fetching items", cause = error)
                    return@addSnapshotListener
                }

                val map = value?.documents!!.mapNotNull { it.toObject(WarrantyItem::class.java) }
                Log.d("FirestoreRepository", map.toString())
                offer(map)
            }
            awaitClose {
                listener.remove()
            }
        }
    }
}
