package com.example.greenifymereloaded.ui.login.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
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
    model: LoginViewModel = viewModel(factory = LocalViewModelFactory.current)
) {
    val context = LocalContext.current as Activity

    val state by model.state.collectAsState()
    val showInfoDialog by model.showDialog.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginState.LoginSuccess) {
            when ((state as LoginState.LoginSuccess).isAdmin) {
                true -> {
                    navController.navigate("admin") {
                        popUpTo("login") { inclusive = true }
                    }
                }

                false -> {
                    navController.navigate("user") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }

    LoginScreenContentPortrait(
        signWithGoogle = {
            model.intentChannel.trySend(
                LoginIntent.SignInWithGoogle(context)
            )
        },
        signWithGithub = {
            model.intentChannel.trySend(
                LoginIntent.SignInWithGitHub(context)
            )
        },
        signInWithEmail = {
            navController.navigate("login_Email")
        },
        openDialog = {
            model.showDialog()
        }
    )

    if (showInfoDialog) Dialog(model::dialogDismissed)

}

@Composable
fun LoginScreenContentPortrait(
    signWithGoogle: () -> Unit = {},
    signWithGithub: () -> Unit = {},
    signInWithEmail: () -> Unit = {},
    openDialog: () -> Unit = {}
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
            TopSection(modifier = Modifier.weight(1f), openDialog)
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
                },
                onClick = signInWithEmail
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
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
            .width(260.dp)
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

/**
 * @param onDismiss Callback when the dialog is dismissed
 */
@Composable
private fun Dialog(onDismiss: () -> Unit = {}) {
    val mikevafeiad045 = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mikevafeiad045"))
    val marinagial = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/marinagial"))
    val tonyGnk = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TonyGnk"))
    val soly02 = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/soly-02"))
    val mppapad = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mppapad"))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.landing_page_dialog_app_information)) },
        text = {
            LazyColumn {
                item {
                    Text(
                        text = stringResource(R.string.landing_page_greenify_me)
                    )
                }
                item { Spacer(modifier = Modifier.height(18.dp)) }
                item { DeveloperRow("mikevafeiad045", true, mikevafeiad045) }
                item { DeveloperRow("marinagial", false, marinagial) }
                item { DeveloperRow("TonyGnk", true, tonyGnk) }
                item { DeveloperRow("mppapad", true, mppapad) }
                item { DeveloperRow("soly-02", false, soly02) }
            }
        },
        confirmButton = { }
    )
}

/**
 * @param name Developer's name
 * @param isMan Gender indicator for the icon
 * @param intent Intent to the developer's profile
 */
@Composable
fun DeveloperRow(name: String, isMan: Boolean, intent: Intent) {
    val context = LocalContext.current
    TextButton(
        onClick = { context.startActivity(intent) },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.height(40.dp)
    ) {
        Icon(
            painter = painterResource(if (isMan) R.drawable.man_head else R.drawable.woman_head),
            contentDescription = name,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(8.dp)
        )
        Text(name, modifier = Modifier.padding(8.dp))
    }
}