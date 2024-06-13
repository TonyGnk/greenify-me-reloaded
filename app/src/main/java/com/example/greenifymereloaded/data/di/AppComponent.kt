package com.example.greenifymereloaded.data.di

import androidx.lifecycle.ViewModelProvider
import com.example.greenifymereloaded.MyApplication
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, RepositoryModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory
    fun inject(myApplication: MyApplication)
}