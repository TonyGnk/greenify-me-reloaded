package com.example.greenifymereloaded.ui.user_form.views

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar
import com.example.greenifymereloaded.ui.common.shared.SharedColumn
import com.example.greenifymereloaded.ui.user_form.UserFormModel
import com.example.greenifymereloaded.ui.user_form.UserFormState

@Composable
fun UserFormMain(model: UserFormModel = viewModel(factory = LocalViewModelFactory.current)) {

    val state by model.state.collectAsState()

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