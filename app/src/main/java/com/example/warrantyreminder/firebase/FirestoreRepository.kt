package com.example.warrantyreminder.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData

import com.example.warrantyreminder.model.WarrantyItem
import com.example.warrantyreminder.model.WarrantyPhoto
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser!!.uid


    fun addWarrantyItem(warrantyItem: WarrantyItem, warrantyItemId: String) {
        firestore.collection("users").document(user).collection("warranty").document(warrantyItemId).set(warrantyItem)
    }

    fun createWarrantyItem(): DocumentReference {
        return firestore.collection("users").document(user).collection("warranty").document()
    }

    fun addPhoto(warrantyItemId: String) {
        firestore.collection("users").document(user).collection("warranty").document(warrantyItemId).collection("photos").document()
    }

    fun updatePhotoDb(warrantyItemId: String, warrantyPhoto: WarrantyPhoto) {
        val document = firestore.collection("users").document(user).collection("warranty").document(warrantyItemId).collection("photos").document()
        val photoId = document.id
        firestore.collection("users").document(user).collection("warranty").document(warrantyItemId).collection("photos").
        document(photoId).set(warrantyPhoto)
    }



    fun deleteItem(warrantyItem: String): Task<Void> {
        return firestore.collection("users").document(user).collection("warranty")
            .document(warrantyItem)
            .delete()
    }

    fun getWarrantyItem(warrantyItemId: String): Flow<WarrantyItem> {
        return callbackFlow {
            val listener = firestore.collection("users").document(user).collection("warranty")
                .document(warrantyItemId).addSnapshotListener { value, _ ->
                    val warrantyItem = value?.toObject<WarrantyItem>()!!
                    offer(warrantyItem)
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    fun queryWarrantyList(): Flow<List<WarrantyItem>> {
        return callbackFlow {
            val listener =
                firestore.collection("users").document(user).collection("warranty").orderBy(
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