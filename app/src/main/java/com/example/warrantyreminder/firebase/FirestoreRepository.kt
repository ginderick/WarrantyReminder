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
import kotlinx.android.synthetic.main.fragment_edit_warranty.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser!!.uid
    private val warrantyCollection = firestore.collection("users").document(user).collection("warranty")


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

    fun deleteItem(warrantyItem: String): Task<Void> {
        warrantyCollection.document(warrantyItem).collection("photos").get().addOnCompleteListener {
            //get the id of the document on the sub-collection photo
            val id = it.result!!.documents[0].id
            warrantyCollection.document(warrantyItem).collection("photos").document(id).delete()
        }
        return warrantyCollection.document(warrantyItem).delete()
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

    fun updateWarrantyItem(warrantyItemId: String, itemName: String, itemDescription: String, expirationDate: Long) {
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

                val map =
                    value?.documents!!.mapNotNull { it.toObject(WarrantyItem::class.java) }
                Log.d("warrantyItem", map.toString())
                offer(map)
            }

            awaitClose {
                listener.remove()
            }
        }
    }
}