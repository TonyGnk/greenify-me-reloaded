package com.example.greenifymereloaded.data.di


import com.example.greenifymereloaded.data.repository.AdminDaoRepository
import com.example.greenifymereloaded.data.repository.AdminDaoRepositoryImpl
import com.example.greenifymereloaded.data.repository.AuthRepository
import com.example.greenifymereloaded.data.repository.AuthRepositoryImpl
import com.example.greenifymereloaded.data.repository.UserDaoRepository
import com.example.greenifymereloaded.data.repository.UserDaoRepositoryImpl
import com.example.greenifymereloaded.data.repository.UserRepository
import com.example.greenifymereloaded.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        userPreferences: UserPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, userPreferences)
    }

    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserDaoRepository(
        firebaseFirestore: FirebaseFirestore,
        userPreferences: UserPreferences
    ): UserDaoRepository {
        return UserDaoRepositoryImpl(firebaseFirestore, userPreferences)
    }

    @Provides
    @Singleton
    fun provideAdminDaoRepository(firebaseFirestore: FirebaseFirestore): AdminDaoRepository {
        return AdminDaoRepositoryImpl(firebaseFirestore)
    }
}