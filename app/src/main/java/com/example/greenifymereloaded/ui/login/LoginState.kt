package com.example.greenifymereloaded.ui.login

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()

    data class LoginSuccess(val isAdmin: Boolean) : LoginState()

    //data class LoginSuccessAdmin(val message: String) : LoginState()
    data class LoginError(val error: String) : LoginState()

    data class LoginPasswordFieldEmpty(val error: String) : LoginState()

    data class LoginEmailFieldEmpty(val error: String) : LoginState()

    data class LoginEmailAndPasswordFieldEmpty(val error: String) : LoginState()

    data class LoginNoRegisteredUser(val error: String) : LoginState()

    data class RegisterSuccess(val message: String) : LoginState()
    data class RegisterError(val error: String) : LoginState()
}