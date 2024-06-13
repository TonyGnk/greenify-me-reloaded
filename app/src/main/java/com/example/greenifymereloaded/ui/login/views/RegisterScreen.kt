package com.example.greenifymereloaded.ui.login.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.ui.login.LoginIntent
import com.example.greenifymereloaded.ui.login.LoginState
import com.example.greenifymereloaded.ui.login.LoginViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(factory = LocalViewModelFactory.current)
) {

    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            viewModel.intentChannel.trySend(
                LoginIntent.Register(
                    email,
                    password
                )
            )
        }) {
            Text("Register")
        }
        when (state) {
            is LoginState.Idle -> {}
            is LoginState.Loading -> CircularProgressIndicator()
            is LoginState.RegisterSuccess -> {
                Text(text = (state as LoginState.RegisterSuccess).message)
            }

            is LoginState.RegisterError -> {
                Text(text = (state as LoginState.RegisterError).error)
            }

            is LoginState.LoginError -> {
                Text(text = (state as LoginState.LoginError).error)
            }

            is LoginState.LoginSuccess -> {
                Text(text = (state as LoginState.LoginSuccess).message)
            }
        }
    }
}