package com.example.greenifymereloaded.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greenifymereloaded.MyApplication
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Composable
fun ProvideViewModelFactory(
    myApplication: MyApplication,
    content: @Composable () -> Unit
) {
    val viewModelFactory = myApplication.getViewModelFactory()
    CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
        content()
    }
}

// LocalViewModelFactory definition
val LocalViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("No ViewModelFactory provided")
}