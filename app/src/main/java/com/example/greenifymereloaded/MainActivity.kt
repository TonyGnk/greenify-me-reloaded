package com.example.greenifymereloaded

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.data.di.ProvideViewModelFactory
import com.example.greenifymereloaded.ui.common.AppNavHost
import com.example.greenifymereloaded.ui.common.AppViewModel
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("StartupTiming", "Start ${System.currentTimeMillis() % 100000}")
        val splashScreen = installSplashScreen()


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ComposeTheme {
                ProvideViewModelFactory(
                    myApplication = LocalContext.current.applicationContext as MyApplication
                ) {
                    val viewModel: AppViewModel = viewModel(factory = LocalViewModelFactory.current)
                    splashScreen.setKeepOnScreenCondition {
                        viewModel.isSplashScreenVisible.value
                    }
                    AppNavHost()
                }
            }
        }
    }
}