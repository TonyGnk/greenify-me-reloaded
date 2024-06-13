package com.example.greenifymereloaded.data.repository

import com.google.firebase.auth.FirebaseAuth

interface UserRepository {
    fun logout(): Result<Unit>
    fun getCurrentUserEmail(): String
}

class UserRepositoryImpl(private val firebaseAuth: FirebaseAuth) : UserRepository {

    override fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserEmail(): String {
        return firebaseAuth.currentUser?.email.orEmpty()
    }

    //override fun getUserPoints(): Int {

}