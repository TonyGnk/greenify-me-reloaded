package com.example.greenifymereloaded.data.di

import android.content.Context
import com.example.greenifymereloaded.MyApplication
import com.example.greenifymereloaded.ui.common.AppViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MyApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideUserPreferences(context: Context): UserPreferences = UserPreferences(context)

    @Provides
    @Singleton
    fun provideAppViewModel(userPreferences: UserPreferences): AppViewModel {
        return AppViewModel(userPreferences)
    }
}