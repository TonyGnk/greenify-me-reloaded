package com.example.greenifymereloaded.ui.login

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class LoginSuccess(val message: String) : LoginState()
    data class LoginError(val error: String) : LoginState()
    data class RegisterSuccess(val message: String) : LoginState()
    data class RegisterError(val error: String) : LoginState()
}