package com.example.greenifymereloaded


import android.app.Application
import com.example.greenifymereloaded.data.di.AppComponent
import com.example.greenifymereloaded.data.di.AppModule
import com.example.greenifymereloaded.data.di.DaggerAppComponent


class MyApplication : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
    }

    fun getViewModelFactory() = appComponent.getViewModelFactory()
}