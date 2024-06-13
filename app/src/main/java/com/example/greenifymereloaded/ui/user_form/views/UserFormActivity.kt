package com.example.greenifymereloaded.ui.user_form.views

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenifymereloaded.MyApplication
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.data.di.ProvideViewModelFactory
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar
import com.example.greenifymereloaded.ui.common.shared.SharedColumn
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme
import com.example.greenifymereloaded.ui.user_form.UserFormModel
import com.example.greenifymereloaded.ui.user_form.UserFormState
import com.example.greenifymereloaded.ui.user_home.UserHomeModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.patrykandpatrick.vico.core.cartesian.data.ChartValues.Empty.model

class UserFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                ProvideViewModelFactory(
                    myApplication = LocalContext.current.applicationContext as MyApplication
                ) {
                    UserFormMain()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun UserFormMain(model: UserFormModel = viewModel(factory = LocalViewModelFactory.current)) {

    val state by model.state.collectAsState()

    if (state.askPermission) {
        val notificationPermissionState = rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        if (!notificationPermissionState.status.isGranted) {
            LaunchedEffect(true) {
                notificationPermissionState.launchPermissionRequest()
            }
        } else {
            //model.submitForm(LocalContext.current as Activity)
        }
    }

    if (state.showDialog) UserFormDialogMain(model, state)
    SharedColumn {
        AppBar(model, state)
        UserFormList(model, state, Modifier.weight(1f))
        UserFormBottomButtons(model, state)
    }
}

@Composable
private fun AppBar(model: UserFormModel, state: UserFormState) {
    val activity = LocalContext.current as Activity
    SharedAppBar(
        text = stringResource(state.strings.appBarLabel),
    ) {
        TextButton(onClick = { model.quitForm(activity) }) {
            Text(text = stringResource(state.strings.appBarCancel))
        }
    }
}