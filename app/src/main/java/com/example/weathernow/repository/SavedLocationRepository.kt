package com.example.weathernow.repository

import com.example.weathernow.model.SavedLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class SavedLocationRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    interface SaveCallback {
        fun onSaved()
        fun onAlreadySaved()
        fun onError(message: String)
    }

    interface StatusCallback {
        fun onResult(isSaved: Boolean)
        fun onError(message: String)
    }

    interface RemoveCallback {
        fun onRemoved()
        fun onError(message: String)
    }

    interface SavedLocationsCallback {
        fun onLocationsChanged(locations: List<SavedLocation>)
        fun onError(message: String)
    }

    private fun getSavedLocationsCollection(): CollectionReference? {
        val uid = auth.currentUser?.uid ?: return null

        return firestore
            .collection("users")
            .document(uid)
            .collection("savedLocations")
    }

    private fun getDocumentId(location: SavedLocation): String {
        return "${location.latitude}_${location.longitude}"
            .replace(".", "_")
    }

    fun saveLocation(
        location: SavedLocation,
        callback: SaveCallback
    ) {

        val collection = getSavedLocationsCollection()

        if (collection == null) {
            callback.onError("You must be signed in.")
            return
        }

        val document = collection.document(
            getDocumentId(location)
        )

        document.get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {

                    callback.onAlreadySaved()

                } else {

                    document.set(location)
                        .addOnSuccessListener {
                            callback.onSaved()
                        }
                        .addOnFailureListener { exception ->
                            callback.onError(
                                exception.message
                                    ?: "Unable to save location."
                            )
                        }
                }
            }
            .addOnFailureListener { exception ->
                callback.onError(
                    exception.message
                        ?: "Unable to check saved location."
                )
            }
    }

    fun isLocationSaved(
        location: SavedLocation,
        callback: StatusCallback
    ) {

        val collection = getSavedLocationsCollection()

        if (collection == null) {
            callback.onResult(false)
            return
        }

        collection
            .document(getDocumentId(location))
            .get()
            .addOnSuccessListener { snapshot ->
                callback.onResult(snapshot.exists())
            }
            .addOnFailureListener { exception ->
                callback.onError(
                    exception.message
                        ?: "Unable to check location."
                )
            }
    }

    fun removeLocation(
        location: SavedLocation,
        callback: RemoveCallback
    ) {

        val collection = getSavedLocationsCollection()

        if (collection == null) {
            callback.onError("You must be signed in.")
            return
        }

        collection
            .document(getDocumentId(location))
            .delete()
            .addOnSuccessListener {
                callback.onRemoved()
            }
            .addOnFailureListener { exception ->
                callback.onError(
                    exception.message
                        ?: "Unable to remove location."
                )
            }
    }

    fun listenToSavedLocations(
        callback: SavedLocationsCallback
    ): ListenerRegistration? {

        val collection = getSavedLocationsCollection()

        if (collection == null) {
            callback.onError("You must be signed in.")
            return null
        }

        return collection.addSnapshotListener { snapshot, exception ->

            if (exception != null) {
                callback.onError(
                    exception.message
                        ?: "Unable to load saved locations."
                )
                return@addSnapshotListener
            }

            val locations = snapshot
                ?.documents
                ?.mapNotNull {
                    it.toObject(SavedLocation::class.java)
                }
                ?: emptyList()

            callback.onLocationsChanged(locations)
        }
    }
}