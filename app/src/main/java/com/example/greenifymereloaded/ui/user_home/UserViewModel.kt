package com.example.greenifymereloaded.ui.user_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.data.repository.UserDaoRepository
import com.example.greenifymereloaded.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class UserHomeModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
    private val userDaoRepository: UserDaoRepository
) : ViewModel() {
    private val _userHomeState = MutableStateFlow(UserPointState(0))
    val userHomeState: StateFlow<UserPointState> = _userHomeState

    private val _account = MutableStateFlow(Account("1", 0))
    //val animatedState: MutableStateFlow<Float> = MutableStateFlow(0f)


    init {
        viewModelScope.launch {
            userPreferences.userId.collect {
                println("User id: $it")
                if (it.isBlank()) return@collect
                _account.value = userDaoRepository.getAccount(it) ?: Account("1", 0)
                _userHomeState.update { state ->
                    state.copy(
                        points = _account.value.points
                    )
                }
            }
        }

//        viewModelScope.launch {
//            delay(400)
//            animatedState.value = _userHomeState.value.percentInLevel
//        }

        viewModelScope.launch {
            delay(300)
            getGreetingTextFromTime()
        }
        viewModelScope.launch {
            delay(2300)
            _userHomeState.update {
                it.copy(greetingText = R.string.app_name)
            }
            //greetingAnimationPlayedUser = true
        }
    }


    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            userPreferences.clear()
        }
    }

    private fun getGreetingTextFromTime() {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val greetingText = when (currentHour) {
            in 6..11 -> R.string.user_good_morning
            else -> R.string.user_good_afternoon
        }
        _userHomeState.update {
            it.copy(greetingText = greetingText)
        }
    }
}