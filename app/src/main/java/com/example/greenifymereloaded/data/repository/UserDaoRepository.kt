package com.example.greenifymereloaded.data.repository

import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.data.model.Form
import com.example.greenifymereloaded.data.model.Material
import com.example.greenifymereloaded.data.model.Track
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

interface UserDaoRepository {
    suspend fun getAccount(id: String): Account?
    suspend fun getMaterialsWithCategory(category: String): List<Material>
    fun saveFormAndTracks(form: Form, trackList: List<Track>, name: String)

    fun addUser(userId: String, name: String)
}

class UserDaoRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val userPreferences: UserPreferences,
) : UserDaoRepository {

    private val accountsCollection = fireStore.collection("accounts")
    private val formsCollection = fireStore.collection("forms")
    private val tracksCollection = fireStore.collection("tracks")
    private val materialsCollection = fireStore.collection("materials")


    override suspend fun getAccount(id: String): Account? {
        if (id.isBlank()) {
            // Return null or throw an exception if the ID is invalid
            throw IllegalArgumentException("Invalid document ID")
        }

        // Fetch the document with the given ID
        val documentSnapshot = accountsCollection.document(id).get().await()

        // Convert the document snapshot to an Account object if it exists
        return if (documentSnapshot.exists()) {
            Account(
                id = id,
                points = (documentSnapshot.data?.get("points") as Long).toInt(),
                name = userPreferences.loginUserName.first()
            )
        } else {
            null // Return null if the document doesn't exist
        }
    }

    override suspend fun getMaterialsWithCategory(category: String): List<Material> {
        val querySnapshot = materialsCollection.whereEqualTo("category", category).get().await()
        return querySnapshot.documents.map { document ->
            val material = document.toObject(Material::class.java)!!
            material.copy(materialId = document.id)
        }
    }


    override fun saveFormAndTracks(form: Form, trackList: List<Track>, name: String) {
        fireStore.runTransaction { transaction ->
            // Create a new document reference with an automatic ID
            val formRef = fireStore.collection("forms").document()
            val newFormId = formRef.id

            // Create a new form object with the generated ID
            val newForm = form.copy(formId = newFormId)

            // Create a new track object with the formId
            val newTracks = trackList.map { track ->
                track.copy(formId = newFormId)
            }

            // Save the form
            transaction.set(formRef, newForm)

            // Save the track with the formId
            val trackRef = fireStore.collection("tracks").document()
            newTracks.forEach { newTrack ->
                transaction.set(trackRef, newTrack)
            }
        }.addOnSuccessListener {
            // Transaction success
            println("Form and Track saved successfully")
        }.addOnFailureListener { e ->
            // Transaction failure
            println("Error saving Form and Track: ${e.message}")
        }
    }

    override fun addUser(userId: String, name: String) {
        val account = Account(
            id = userId,
            points = 0,
            name = name
        )
        accountsCollection.document(userId).set(account)
    }

}