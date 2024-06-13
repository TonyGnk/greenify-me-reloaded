package com.example.greenifymereloaded.ui.user_home.views

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar
import com.example.greenifymereloaded.ui.common.shared.SharedLazyColumn
import com.example.greenifymereloaded.ui.user_form.views.UserFormActivity
import com.example.greenifymereloaded.ui.user_home.UserHomeModel
import com.example.greenifymereloaded.ui.user_home.UserLevel

@Composable
fun UserScreen(
    navController: NavController,
    model: UserHomeModel = viewModel(factory = LocalViewModelFactory.current)
) {
    val context = LocalContext.current
    val state by model.userHomeState.collectAsState()
    UserScreenContent(
        onLogOutClick = {
            model.logout()
            navController.navigate("login") {
                popUpTo("user") { inclusive = true }
            }
        },
        points = state.points,
        pointsInLevel = state.pointsInLevel,
        targetPointsInLevel = state.targetPointsInLevel,
        targetingLevel = state.targetingLevel,
        animatedPercentInLevel = state.percentInLevel,
        onFabPressed = {
            val intent = Intent(context, UserFormActivity::class.java)
            context.startActivity(intent)
        },
        appBarLabel = stringResource(state.greetingText)
    )
}

@Composable
fun UserScreenContent(
    onLogOutClick: () -> Unit,
    points: Int,
    pointsInLevel: Int,
    targetPointsInLevel: Int,
    targetingLevel: UserLevel,
    animatedPercentInLevel: Float,
    onFabPressed: () -> Unit,
    appBarLabel: String
) {
    SharedLazyColumn(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onFabPressed,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Outlined.Add, "")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Form")
            }
        }
    ) {
        item {
            AppBar(
                text = appBarLabel,
                onLogOutClick = onLogOutClick
            )
        }
        item { TipWidget() }
        item {
            CitizenPoints(
                points = points,
                pointsInLevel = pointsInLevel,
                targetPointsInLevel = targetPointsInLevel,
                targetingLevel = targetingLevel,
                animatedPercentInLevel = animatedPercentInLevel
            )
        }
    }

//    if (!state.hasShowedOnce && !account.hasIntroViewed) {
//        WelcomeDialog {
//            model.setShouldShowOnce()
//        }
//    }
}

/**
 * This composable represents an app bar with optional animation and an exit button.
 *
 * @param text Text to display in the app bar.
 */
@Composable
private fun AppBar(
    text: String = "Label",
    onLogOutClick: () -> Unit
) {

    SharedAppBar(text = text, isTextAnimated = true) {
        IconButton(
            onClick = onLogOutClick,
            content = {
                Icon(
                    painterResource(id = R.drawable.exit),
                    contentDescription = "LogOut",
                    modifier = Modifier.width(22.dp)
                )
            }
        )
    }
}