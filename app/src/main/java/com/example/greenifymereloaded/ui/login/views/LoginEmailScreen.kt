package com.example.greenifymereloaded.ui.login.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar
import com.example.greenifymereloaded.ui.common.shared.SharedBackBehavior
import com.example.greenifymereloaded.ui.common.shared.SharedColumn
import com.example.greenifymereloaded.ui.login.LoginIntent
import com.example.greenifymereloaded.ui.login.LoginState
import com.example.greenifymereloaded.ui.login.LoginViewModel

@Composable
fun LoginEmailScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(factory = LocalViewModelFactory.current)
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginState.LoginSuccess) {
            when ((state as LoginState.LoginSuccess).isAdmin) {
                true -> {
                    navController.navigate("admin") {
                        popUpTo("login_Email") { inclusive = true }
                        popUpTo("login") { inclusive = true }
                    }
                }

                false -> {
                    navController.navigate("user") {
                        popUpTo("login_Email") { inclusive = true }
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }
    LoginEmailScreenContent(
        navController = navController,
        onLogin = { email, password ->
            viewModel.intentChannel.trySend(
                LoginIntent.Login(email, password)
            )
        },
        onRegister = { email, password, name ->
            viewModel.intentChannel.trySend(
                LoginIntent.Register(email, password, name)
            )
        },
        state = state
    )
}

@Composable
fun LoginEmailScreenContent(
    navController: NavController,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String) -> Unit,
    state: LoginState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var nameFieldVisibility by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    SharedColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        SharedAppBar(
            text = "Login using Email",
            backBehavior = SharedBackBehavior.Enable(
                onClick = {
                    if (nameFieldVisibility) nameFieldVisibility = false
                    else navController.popBackStack()
                }
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            isError = state is LoginState.LoginEmailAndPasswordFieldEmpty || state is LoginState.LoginError || state is LoginState.LoginNoRegisteredUser || state is LoginState.LoginEmailFieldEmpty,
            label = { Text("Email") },
            shape = RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.envelope),
                    contentDescription = "Email",
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding)),
        )

        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = nameFieldVisibility) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                shape = RoundedCornerShape(
                    dimensionResource(
                        R.dimen.column_card_corner_radius
                    )
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Email",
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding)),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = state is LoginState.LoginEmailAndPasswordFieldEmpty || state is LoginState.LoginError || state is LoginState.LoginNoRegisteredUser || state is LoginState.LoginPasswordFieldEmpty,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.pen_field),
                    contentDescription = "Password",
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.eye) // Update this with your visible icon resource
                else
                    painterResource(id = R.drawable.eye_crossed) // Update this with your invisible icon resource

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = description,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = { onLogin(email, password) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding)),
        )


        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.weight(1f))


        AnimatedVisibility(visible = !nameFieldVisibility) {
            TextButton(
                onClick = { nameFieldVisibility = !nameFieldVisibility },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding)),
            ) {
                Text("Register")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = nameFieldVisibility) {
            Button(
                onClick = { onRegister(email, password, name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding)),
            ) {
                Text("Register")
            }
        }
        AnimatedVisibility(visible = !nameFieldVisibility) {
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding)),
            ) {
                Text("Sign In")
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
    }
}