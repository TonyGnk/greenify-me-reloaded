package com.example.greenifymereloaded.ui.admin.notifications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.example.greenifymereloaded.MyApplication
import com.example.greenifymereloaded.data.di.ProvideViewModelFactory
import com.example.greenifymereloaded.ui.admin.notifications.views.AdminNotificationsScreen
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme

class AdminNotificationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                ProvideViewModelFactory(
                    myApplication = LocalContext.current.applicationContext as MyApplication
                ) {
                    AdminNotificationsScreen()
                }
            }
        }
    }
}