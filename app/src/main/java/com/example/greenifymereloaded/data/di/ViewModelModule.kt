package com.example.greenifymereloaded.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greenifymereloaded.ui.common.AppViewModel
import com.example.greenifymereloaded.ui.login.LoginViewModel
import com.example.greenifymereloaded.ui.user_form.UserFormModel
import com.example.greenifymereloaded.ui.user_home.UserHomeModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserHomeModel::class)
    abstract fun bindUserViewModel(userHomeModel: UserHomeModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserFormModel::class)
    abstract fun bindUserFormModel(userFormModel: UserFormModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    abstract fun bindAppViewModel(appViewModel: AppViewModel): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)