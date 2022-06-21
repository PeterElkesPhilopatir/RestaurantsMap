package com.peter.restaurantsmap.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.peter.restaurantsmap.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val LOCATION = "location"

class FirestoreLocation @Inject constructor(db: FirebaseFirestore) {

    private val collection = db.collection(LOCATION)
    fun getLocations(liveData: MutableLiveData<List<Location>>) {
      collection.get().addOnSuccessListener { result ->
            var array = ArrayList<Location>()
            for (document in result)
                if (document.exists()) {
                    val location: Location by lazy { Location("", 0.0, 0.0) }
                    location.longitude = document.get("longitude") as Double
                    location.latitude = document.get("latitude") as Double
                    location.name = document.get("name") as String
                    array.add(location)
                }
            liveData.postValue(array)

        }.addOnFailureListener { e ->
        }
    }

    fun addLocation(location: Location): Boolean {
        return try {
           collection.add(location.hash())
                .addOnSuccessListener { documentReference ->
                    Log.d("ADD", documentReference.id)
                }
            true
        } catch (e: Exception) {
            false
        }

    }

}