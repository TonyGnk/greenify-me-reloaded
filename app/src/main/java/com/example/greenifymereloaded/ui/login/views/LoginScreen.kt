package com.example.greenifymereloaded.ui.login.views

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.ui.common.shared.SharedColumn
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme
import com.example.greenifymereloaded.ui.login.LoginIntent
import com.example.greenifymereloaded.ui.login.LoginState
import com.example.greenifymereloaded.ui.login.LoginViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(factory = LocalViewModelFactory.current)
) {
    val context = LocalContext.current as Activity

    val state by viewModel.state.collectAsState()
    //var email by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is LoginState.LoginSuccess) {
            navController.navigate("user") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LoginScreenContentPortrait(
        signWithGoogle = {
            viewModel.intentChannel.trySend(
                LoginIntent.SignInWithGoogle(context)
            )
        },
        signWithGithub = {
            viewModel.intentChannel.trySend(
                LoginIntent.SignInWithGitHub(context)
            )
        }
    )
}

@Composable
fun LoginScreenContentPortrait(
    signWithGoogle: () -> Unit = {},
    signWithGithub: () -> Unit = {}
) {
    SharedColumn {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    dimensionResource(id = R.dimen.horizontalScreenPadding)
                )
                .fillMaxWidth()
        ) {
            TopSection(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))
            AuthenticationProviderWidget(
                label = "Continue with Google",
                image = ProviderIconType.Single {
                    Image(
                        painterResource(id = R.drawable.icons8_google),
                        contentDescription = null,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                },
                onClick = { signWithGoogle() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthenticationProviderWidget(
                label = "Continue with Github",
                image = ProviderIconType.Adaptive(
                    {
                        Image(
                            painterResource(id = R.drawable.github_logo),
                            contentDescription = null,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    },
                    {
                        Image(
                            painterResource(id = R.drawable.github_icon_white),
                            contentDescription = null,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    },
                ),
                onClick = { signWithGithub() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthenticationProviderWidget(
                label = "Continue with Email",
                image = ProviderIconType.Single {
                    Icon(
                        painter = painterResource(id = R.drawable.envelope),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 7.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    //    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") }
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { viewModel.intentChannel.trySend(LoginIntent.Login(email, password)) }) {
//            Text("Login")
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { navController.navigate("register") }) {
//            Text("Register")
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { viewModel.intentChannel.trySend(LoginIntent.SignInWithGoogle(context)) }) {
//            Text("Sign In with Google")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        when (state) {
//            is LoginState.Idle -> {}
//            is LoginState.Loading -> CircularProgressIndicator()
//            is LoginState.LoginSuccess -> {
//                Text(text = (state as LoginState.LoginSuccess).message)
//            }
//
//            is LoginState.LoginError -> {
//                Text(text = (state as LoginState.LoginError).error)
//            }
//
//            is LoginState.RegisterError -> {
//                Text(text = (state as LoginState.RegisterError).error)
//            }
//
//            is LoginState.RegisterSuccess -> {
//                Text(text = (state as LoginState.RegisterSuccess).message)
//            }
//        }
//    }
}

@Composable
private fun AuthenticationProviderWidget(
    image: ProviderIconType,
    label: String,
    onClick: () -> Unit = {}
) {
    val isDarkTheme = isSystemInDarkTheme()

    Surface(
        shape = CircleShape,
        color = when (isDarkTheme) {
            true -> Color(0xFF131314)
            false -> Color(0xFFFFFFFF)
        },
        modifier = Modifier
            .height(50.dp)
            .width(240.dp)
            .clip(CircleShape)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = when (isDarkTheme) {
                        true -> Color(0xFF8E918F)
                        false -> Color.Transparent
                    }
                ),
                shape = CircleShape
            )
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Spacer(modifier = Modifier.width(14.dp))
            when (image) {
                is ProviderIconType.Single -> image.image()
                is ProviderIconType.Adaptive -> {
                    when (isDarkTheme) {
                        true -> image.darkImage()
                        false -> image.lightImage()
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = label)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * @param onInfoClicked Callback when the info icon is clicked
 * @param modifier Modifier to be applied to the composable
 */
@Composable
private fun TopSection(modifier: Modifier = Modifier, onInfoClicked: () -> Unit = {}) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val imageSize = screenHeight * 0.33f

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onInfoClicked,
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "",
                        tint = Color.Gray,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.ic_landing_launcher_foreground),
                contentDescription = "",
                modifier = Modifier.size(imageSize)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to GreenifyMe",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview
@Composable
fun PreviewLoginScreen() {
    ComposeTheme(darkTheme = true) {
        LoginScreenContentPortrait { }
    }
}

private sealed class ProviderIconType {
    data class Single(val image: @Composable () -> Unit) : ProviderIconType()
    data class Adaptive(
        val lightImage: @Composable () -> Unit,
        val darkImage: @Composable () -> Unit
    ) : ProviderIconType()
}