package com.example.greenifymereloaded.data.repository

import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.data.model.Form
import com.example.greenifymereloaded.data.model.Material
import com.example.greenifymereloaded.data.model.RecyclingCategory
import com.example.greenifymereloaded.data.model.Track
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

interface AdminDaoRepository {
    // suspend fun getAccount(id: String): Account?

    fun getTotalPoints(): Flow<Int>

    fun getTop3Accounts(): Flow<List<Pair<String, Int>>>

    fun getAccountsOrderByPoints(): Flow<List<Account>>

    //Category and Total Quantity
    suspend fun getSumQuantityPerCategory(): Map<RecyclingCategory, Int>

    suspend fun getSumQuantityPerMaterialInCategory(category: RecyclingCategory): List<Pair<String, Int>>

    suspend fun getMaterialTotal(): List<Triple<String, RecyclingCategory, Int>>

    fun getUnseenForms(scope: CoroutineScope): Flow<List<FormWithAccountName>>

    suspend fun getTracksPerForm(formId: String): List<TracksWithMaterial>

    suspend fun addPointsToAccount(account: Account, points: Int)

    suspend fun setFormViewed(formId: String)
}

class AdminDaoRepositoryImpl(
    private val fireStore: FirebaseFirestore
) : AdminDaoRepository {

    private val accountsCollection = fireStore.collection("accounts")
    private val formsCollection = fireStore.collection("forms")
    private val tracksCollection = fireStore.collection("tracks")
    private val materialsCollection = fireStore.collection("materials")

    override suspend fun getSumQuantityPerMaterialInCategory(category: RecyclingCategory): List<Pair<String, Int>> {
        // Fetch all materials in the given category
        val materialSnapshots = materialsCollection
            .whereEqualTo("category", category.name)
            .get()
            .await()

        val materialsInCategory = materialSnapshots.documents.map { document ->
            document.toObject(Material::class.java)!!.copy(materialId = document.id)
        }

        // Get the IDs of these materials
        val materialIdsInCategory = materialsInCategory.map { it.materialId }

        // Fetch all tracks that match the material IDs
        val trackSnapshots = tracksCollection
            .whereIn("materialId", materialIdsInCategory)
            .get()
            .await()

        val tracks = trackSnapshots.toObjects(Track::class.java)

        // Sum the quantity per material
        val materialIdToTotalQuantity = tracks.groupBy { it.materialId }
            .mapValues { (_, tracks) -> tracks.sumOf { it.quantity.toInt() } }

        // Create the result list with material names and their corresponding total quantity
        val result = materialIdToTotalQuantity.mapNotNull { (materialId, totalQuantity) ->
            val materialName = materialsInCategory.find { it.materialId == materialId }?.name
            if (materialName != null) {
                Pair(materialName, totalQuantity)
            } else null
        }

        return result
    }

    override fun getTotalPoints(): Flow<Int> = callbackFlow {
        val listener = accountsCollection.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                println("Error getting total points: $e")
                close(e)
            } else {
                println("Total points snapshot: $snapshot")
                val totalPoints = snapshot.documents.sumOf { it.getLong("points")?.toInt() ?: 0 }
                trySend(totalPoints).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }


    override fun getTop3Accounts(): Flow<List<Pair<String, Int>>> = callbackFlow {
        val listener = accountsCollection
            .orderBy("points", Query.Direction.DESCENDING)
            .limit(3)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e) // Close the flow with the exception
                } else if (snapshot != null) {
                    val topAccounts = snapshot.documents.mapNotNull {
                        val name = it.getString("name")
                        val points = it.getLong("points")?.toInt()
                        if (name != null && points != null) {
                            Pair(name, points)
                        } else null // Filter out documents with null fields
                    }
                    trySend(topAccounts).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }


    override fun getAccountsOrderByPoints(): Flow<List<Account>> = callbackFlow {
        val listener = accountsCollection.orderBy("points", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    close(e)
                } else {
                    // Don't use toObjects directly because it will throw an exception if the document is missing a field
                    // Get the name and points fields manually
                    val accounts = snapshot.documents.mapNotNull { document ->
                        val name = document.getString("name")
                        val points = document.getLong("points")?.toInt()
                        if (name != null && points != null) {
                            Account(document.id, points, name)
                        } else null
                    }
                    trySend(accounts).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getSumQuantityPerCategory(): Map<RecyclingCategory, Int> {
        val trackSnapshots = tracksCollection.get().await()

        //Manually convert to Tracks but for form id
        val tracks = trackSnapshots.toObjects(Track::class.java)

        // Fetch all materials
        val materialSnapshots = materialsCollection.get().await()
        val materials = materialSnapshots.documents.map { document ->
            document.toObject(Material::class.java)!!.copy(materialId = document.id)
        }

        // Map material IDs to their corresponding categories
        val materialIdToCategory = materials.associateBy({ it.materialId }, { it.category })

        // Group tracks by material category and sum the quantities
        val categoryToTotalQuantity = mutableMapOf<RecyclingCategory, Int>()

        for (track in tracks) {
            val category = materialIdToCategory[track.materialId] ?: RecyclingCategory.OTHER
            val currentTotal = categoryToTotalQuantity[category] ?: 0
            categoryToTotalQuantity[category] = currentTotal + track.quantity.toInt()
        }

        //categoryToTotalQuantity.map { Pair(it.key, it.value) } replace for return type Map
        return categoryToTotalQuantity
    }

    override suspend fun getMaterialTotal(): List<Triple<String, RecyclingCategory, Int>> {
        // Fetch all tracks
        val trackSnapshots = tracksCollection.get().await()
        val tracks = trackSnapshots.toObjects(Track::class.java)

        // Fetch all materials
        val materialSnapshots = materialsCollection.get().await()
        val materials = materialSnapshots.documents.map { document ->
            document.toObject(Material::class.java)!!.copy(materialId = document.id)
        }

        // Create a map of materialId to material details (name and category)
        val materialDetailsMap = materials.associateBy(
            { it.materialId },
            { Pair(it.name, it.category) }
        )

        // Aggregate points per material
        val materialPointsMap = mutableMapOf<String, Int>()

        for (track in tracks) {
            val materialId = track.materialId
            val currentPoints = materialPointsMap[materialId] ?: 0
            materialPointsMap[materialId] = (currentPoints + track.quantity).toInt()
        }

        // Create the result list of Triple<Material Name, Category, TotalPoints>
        val result = mutableListOf<Triple<String, RecyclingCategory, Int>>()
        for ((materialId, totalPoints) in materialPointsMap) {
            val (name, category) = materialDetailsMap[materialId] ?: continue
            result.add(Triple(name, category, totalPoints))
        }

        return result
    }

    private suspend fun getAccountName(id: String): Account {
        //Get the account with id. If null there in no account with that id
        return Account(
            id = id,
            name = accountsCollection.document(id).get().await().getString("name") ?: "No account",
            points = accountsCollection.document(id).get().await().getLong("points")?.toInt() ?: 0
        )
    }


    override fun getUnseenForms(scope: CoroutineScope): Flow<List<FormWithAccountName>> =
        callbackFlow {
            val listener = formsCollection
                .whereEqualTo("hasAdminViewed", false)
                // .limit(6)
                .addSnapshotListener { snapshot, e ->
                    if (e != null || snapshot == null) {
                        close(e) // Close the flow with the exception
                    } else {
                        val unseenForms = snapshot.toObjects(Form::class.java)

                        scope.launch {
                            val formWithAccountName = unseenForms.map { form ->
                                val accountName = getAccountName(form.accountId)
                                FormWithAccountName(form, accountName)
                            }
                            trySend(formWithAccountName).isSuccess
                        }
                    }
                }
            awaitClose { listener.remove() }
        }

    private suspend fun getMaterial(materialId: String): Material {
        return materialsCollection.document(materialId).get().await()
            .toObject(Material::class.java)!!
    }

    override suspend fun getTracksPerForm(formId: String): List<TracksWithMaterial> {
        val tracks = tracksCollection.whereEqualTo("formId", formId).get().await()
            .toObjects(Track::class.java)
        return tracks.map { track ->
            val material = getMaterial(track.materialId)
            TracksWithMaterial(track, material)
        }
    }

    override suspend fun addPointsToAccount(account: Account, points: Int) {
        val currentPoints = account.points
        val newPoints = currentPoints + points
        accountsCollection.document(account.id).update("points", newPoints).await()
    }

    override suspend fun setFormViewed(formId: String) {
        formsCollection.document(formId).update("hasAdminViewed", true).await()
    }
}

data class FormWithAccountName(
    val form: Form,
    val account: Account
)

data class TracksWithMaterial(
    val track: Track,
    val material: Material
)