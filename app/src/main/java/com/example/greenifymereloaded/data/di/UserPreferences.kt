package com.example.greenifymereloaded.data.di

// UserPreferences.kt
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UserType {
    USER, ADMIN, NONE
}

class UserPreferences(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    private val LOGIN_STATUS_KEY = booleanPreferencesKey("login_status")
    private val IS_ADMIN = booleanPreferencesKey("is_admin")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("user_name")


    val loginUserName: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_NAME] ?: ""
        }

    val loginAllCombined: Flow<UserType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            if (preferences[LOGIN_STATUS_KEY] == true) {
                if (preferences[IS_ADMIN] == true) {
                    UserType.ADMIN
                } else {
                    UserType.USER
                }
            } else {
                UserType.NONE
            }
        }


    val userId: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_ID_KEY] ?: ""
        }

    suspend fun saveLoginStatus(status: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = status
        }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun saveIsAdmin(isAdmin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ADMIN] = isAdmin
        }
    }

    suspend fun saveUserId(id: String) {
        println("User id: $id")
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}