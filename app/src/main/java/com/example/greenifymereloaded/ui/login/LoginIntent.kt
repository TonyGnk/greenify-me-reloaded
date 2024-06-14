package com.example.greenifymereloaded.ui.login

import android.app.Activity
import android.content.Context

sealed class LoginIntent {
    data class Login(val email: String, val password: String) : LoginIntent()

    data class Register(val email: String, val password: String, val name: String) : LoginIntent()
    data class SignInWithGitHub(val activity: Activity) : LoginIntent()
    data class SignInWithGoogle(val context: Context) : LoginIntent()
}