package com.example.greenifymereloaded.ui.user_form

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.example.greenifymereloaded.MyApplication
import com.example.greenifymereloaded.data.di.ProvideViewModelFactory
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme
import com.example.greenifymereloaded.ui.user_form.views.UserFormMain

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