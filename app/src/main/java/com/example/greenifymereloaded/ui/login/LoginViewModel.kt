package com.example.greenifymereloaded.ui.login

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _intentChannel = Channel<LoginIntent>(Channel.UNLIMITED)
    val intentChannel = _intentChannel

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> get() = _state

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is LoginIntent.Login -> login(intent.email, intent.password)
                    is LoginIntent.Register -> register(intent.email, intent.password)
                    is LoginIntent.SignInWithGitHub -> signInWithGitHub(intent.activity)
                    is LoginIntent.SignInWithGoogle -> handleGoogleSignIn(intent.context)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            authRepository.login(email, password).collect { result ->
                result.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess("Login successful!")
                    },
                    onFailure = { e ->
                        _state.value = LoginState.LoginError(e.message ?: "An error occurred")
                    }
                )
            }
        }
    }

    private fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            authRepository.register(email, password).collect { result ->
                result.fold(
                    onSuccess = {
                        _state.value = LoginState.RegisterSuccess("Registration successful!")
                    },
                    onFailure = { e ->
                        _state.value = LoginState.RegisterError(e.message ?: "An error occurred")
                    }
                )
            }
        }
    }

    private fun signInWithGitHub(activity: Activity) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            authRepository.signInWithGitHub(activity).collect { result ->
                result.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess("GitHub sign-in successful!")
                    },
                    onFailure = { e ->
                        _state.value = LoginState.LoginError(e.message ?: "An error occurred")
                    }
                )
            }
        }
    }

    private fun handleGoogleSignIn(context: Context) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            authRepository.handleGoogleSignIn(context).collect { result ->
                result.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess("Google sign-in successful!")
                    },
                    onFailure = { e ->
                        _state.value = LoginState.LoginError(e.message ?: "An error occurred")
                    }
                )
            }
        }
    }
}