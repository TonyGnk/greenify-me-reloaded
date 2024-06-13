package com.example.greenifymereloaded.data.repository

import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.data.model.Material
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface UserDaoRepository {
    suspend fun getAccount(id: String): Account?
    suspend fun getMaterialsWithCategory(category: String): List<Material>
}

class UserDaoRepositoryImpl(
    private val fireStore: FirebaseFirestore
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
                points = (documentSnapshot.data?.get("points") as Long).toInt()
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

}