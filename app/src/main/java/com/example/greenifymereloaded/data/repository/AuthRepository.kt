package com.example.greenifymereloaded.data.repository


import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.greenifymereloaded.data.di.UserPreferences
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Result<AuthResult>>
    suspend fun register(email: String, password: String): Flow<Result<AuthResult>>
    suspend fun signInWithGitHub(activity: Activity): Flow<Result<AuthResult>>
    suspend fun handleGoogleSignIn(context: Context): Flow<Result<AuthResult>>
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Result<AuthResult>> =
        callbackFlow {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                trySend(Result.success(authResult))
            } catch (e: Exception) {
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override suspend fun register(email: String, password: String): Flow<Result<AuthResult>> =
        callbackFlow {
            try {
                val authResult =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                trySend(Result.success(authResult))
            } catch (e: Exception) {
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

//    override suspend fun signInWithGitHub(
//        activity: Activity
//    ): Flow<Result<AuthResult>> = callbackFlow {
//        val provider = OAuthProvider.newBuilder("github.com")
//        try {
//            val authResult = firebaseAuth.startActivityForSignInWithProvider(
//                activity, provider.build()
//            ).await()
//            trySend(Result.success(authResult))
//        } catch (e: Exception) {
//            trySend(Result.failure(e))
//        }
//        awaitClose { }
//    }

    override suspend fun signInWithGitHub(activity: Activity): Flow<Result<AuthResult>> =
        callbackFlow {
            val provider = OAuthProvider.newBuilder("github.com")
            try {
                val authResult =
                    firebaseAuth.startActivityForSignInWithProvider(activity, provider.build())
                        .await()
                userPreferences.saveLoginStatus(true)
                userPreferences.saveUserId(authResult.user?.email ?: "")
                trySend(Result.success(authResult))
            } catch (e: Exception) {
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    //    override suspend fun handleGoogleSignIn(context: Context): Flow<Result<AuthResult>> =
//        callbackFlow {
//            try {
//                val credentialManager = CredentialManager.create(context)
//                val ranNonce = UUID.randomUUID().toString()
//                val bytes = ranNonce.toByteArray()
//                val md = MessageDigest.getInstance("SHA-256")
//                val digest = md.digest(bytes)
//                val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
//
//                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//                    .setFilterByAuthorizedAccounts(false)
//                    .setServerClientId("459025220752-sua1j3go662tunsjstb1vtjd4h11t1ch.apps.googleusercontent.com")
//                    .setNonce(hashedNonce)
//                    .build()
//
//                val request: GetCredentialRequest = GetCredentialRequest.Builder()
//                    .addCredentialOption(googleIdOption)
//                    .build()
//
//                val result = credentialManager.getCredential(context, request)
//                val credential = result.credential
//
//                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                    val googleIdTokenCredential =
//                        GoogleIdTokenCredential.createFrom(credential.data)
//                    val authCredential =
//                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
//                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
//                    trySend(Result.success(authResult))
//                } else {
//                    throw RuntimeException("Received an invalid credential type")
//                }
//            } catch (e: GetCredentialCancellationException) {
//                trySend(Result.failure(Exception("Sign-in was canceled. Please try again.")))
//            } catch (e: Exception) {
//                trySend(Result.failure(e))
//            }
//            awaitClose { }
//        }
    override suspend fun handleGoogleSignIn(context: Context): Flow<Result<AuthResult>> =
        callbackFlow {
            try {
                val credentialManager = CredentialManager.create(context)
                val ranNonce = UUID.randomUUID().toString()
                val bytes = ranNonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(bytes)
                val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("459025220752-sua1j3go662tunsjstb1vtjd4h11t1ch.apps.googleusercontent.com")
                    .setNonce(hashedNonce)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    userPreferences.saveLoginStatus(true)
                    userPreferences.saveUserId(authResult.user?.uid ?: "")
                    trySend(Result.success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type")
                }
            } catch (e: GetCredentialCancellationException) {
                trySend(Result.failure(Exception("Sign-in was canceled. Please try again.")))
            } catch (e: Exception) {
                trySend(Result.failure(e))
            }
            awaitClose { }
        }
}