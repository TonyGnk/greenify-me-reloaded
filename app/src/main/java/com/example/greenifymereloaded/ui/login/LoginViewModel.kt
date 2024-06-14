package com.example.greenifymereloaded.ui.login

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.repository.AuthRepository
import com.example.greenifymereloaded.data.repository.UserDaoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
    private val userDaoRepository: UserDaoRepository
) : ViewModel() {

    private val _intentChannel = Channel<LoginIntent>(Channel.UNLIMITED)
    val intentChannel = _intentChannel

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    val showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        handleIntents()
    }

    fun dialogDismissed() {
        showDialog.value = false
    }

    fun showDialog() {
        showDialog.value = true
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is LoginIntent.Login -> login(intent.email, intent.password)
                    is LoginIntent.Register -> register(intent.email, intent.password, intent.name)
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
                result.first.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess(result.second)
                    },
                    onFailure = { e ->
                        //Catch the e and check if it is a specific error
                        when (e.message) {
                            "The email address is badly formatted." -> {
                                _state.value =
                                    LoginState.LoginEmailFieldEmpty("Email field is empty")
                            }

                            "The password is invalid or the user does not have a password." -> {
                                _state.value =
                                    LoginState.LoginPasswordFieldEmpty("Password field is empty")
                            }

                            "There is no user record corresponding to this identifier. The user may have been deleted." -> {
                                _state.value =
                                    LoginState.LoginNoRegisteredUser("No registered user with this email")
                            }

                            else -> {
                                _state.value =
                                    LoginState.LoginError(e.message ?: "An error occurred")
                            }
                        }
                    }
                )
            }
        }
    }

    private fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            authRepository.register(email, password, name).collect { result ->
                result.first.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess(result.second)
                        addUserIfNotExists()
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
                result.first.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess(result.second)
                        addUserIfNotExists()
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
            authRepository.handleGoogleSignIn(context).collect { resultPair ->
                resultPair.first.fold(
                    onSuccess = {
                        _state.value = LoginState.LoginSuccess(resultPair.second)
                        addUserIfNotExists()
                    },
                    onFailure = { e ->
                        _state.value = LoginState.LoginError(e.message ?: "An error occurred")
                    }
                )
            }
        }
    }


    private fun addUserIfNotExists() {
        viewModelScope.launch {
            val userId = userPreferences.userId.first()
            val userName = userPreferences.loginUserName.first()
            if (userId.isBlank()) return@launch
            val account = userDaoRepository.getAccount(userId)
            if (account == null) {
                println("Adding user with id: $userId and name: $userName")
                userDaoRepository.addUser(userId, userName)
            }
            println("User already exists with id: $userId and name: $userName")
        }
    }

}