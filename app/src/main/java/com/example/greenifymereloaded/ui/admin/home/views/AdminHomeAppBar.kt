package com.example.greenifymereloaded.ui.admin.home.views

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar

@Composable
fun AdminHomeAppBar(
    text: String = "AppBar Text",
    onNotificationsIcon: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    SharedAppBar(
        isTextAnimated = true, text = text,
    ) {
        IconButton(onNotificationsIcon) {
            Icon(
                painter = painterResource(R.drawable.bell),
                contentDescription = stringResource(R.string.admin_home_app_bar_notifications),
                modifier = Modifier.size(22.dp)
            )
        }

        IconButton(onExit) {
            Icon(
                painter = painterResource(R.drawable.exit),
                contentDescription = stringResource(R.string.admin_home_app_bar_exit),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}