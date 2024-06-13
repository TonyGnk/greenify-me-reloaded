package com.example.greenifymereloaded.ui.common

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.ui.login.views.LoginScreen
import com.example.greenifymereloaded.ui.login.views.RegisterScreen
import com.example.greenifymereloaded.ui.user_home.views.UserScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun AppNavHost(
    viewModel: AppViewModel = viewModel(factory = LocalViewModelFactory.current)
) {
    val navController = rememberNavController()
    val startDestination by viewModel.startDestination.collectAsState()

    val enterAnimation =
        slideInHorizontally(
            initialOffsetX = { 40 },
        ) + fadeIn(
            animationSpec = tween(
                200, easing = FastOutSlowInEasing
            )
        )


    val exitAnimation = slideOutHorizontally(
        targetOffsetX = { 40 },
    ) + fadeOut(
        animationSpec = tween(
            200, easing = FastOutSlowInEasing
        )
    )



    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { enterAnimation },
        exitTransition = { exitAnimation },
        popEnterTransition = { enterAnimation },
        popExitTransition = { exitAnimation }
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("user") {
            UserScreen(navController)
        }
        composable("form") {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        }
    }
}

class AppViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isSplashScreenVisible = MutableStateFlow(true)
    val isSplashScreenVisible = _isSplashScreenVisible.asStateFlow()

    val startDestination = MutableStateFlow("login")

    init {
        viewModelScope.launch {
            Log.d("StartupTiming", "Init  ${System.currentTimeMillis() % 100000}")
            val isLoggedIn = userPreferences.loginStatus.first()
            startDestination.value = if (isLoggedIn) "user" else "login"
            delay(150)
            _isSplashScreenVisible.value = false
            Log.d("StartupTiming", "Ready ${System.currentTimeMillis() % 100000}")
        }
    }
}