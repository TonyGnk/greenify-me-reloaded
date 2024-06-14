package com.example.greenifymereloaded.ui.admin.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.data.repository.AdminDaoRepository
import com.example.greenifymereloaded.data.repository.FormWithAccountName
import com.example.greenifymereloaded.ui.admin.notifications.views.AdminNotificationState
import com.example.greenifymereloaded.ui.admin.notifications.views.AdminNotificationState2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdminNotificationViewModel @Inject constructor(
    private val adminDaoRepository: AdminDaoRepository
) : ViewModel() {

    val state = MutableStateFlow(
        AdminNotificationState2()
    )


    val formsFlow: StateFlow<AdminNotificationState> =
        adminDaoRepository.getUnseenForms(viewModelScope)
            .map { AdminNotificationState(it) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = AdminNotificationState(emptyList())
            )


    fun onNotificationClicked(formWithAccountName: FormWithAccountName) {
        state.update {
            it.copy(
                selectedNotification = formWithAccountName,
                dialogVisible = true
            )
        }
        viewModelScope.launch {
            state.update {
                it.copy(
                    tracks = adminDaoRepository.getTracksPerForm(formWithAccountName.form.formId)
                )
            }
        }
    }


    fun setFormViewedAddPoints() {
        viewModelScope.launch {
            adminDaoRepository.addPointsToAccount(
                account = state.value.selectedNotification?.account!!,
                points = state.value.tracks.sumOf { it.track.quantity }.toInt()
            )
            adminDaoRepository.setFormViewed(state.value.selectedNotification?.form?.formId!!)
        }
    }

    fun onDismissRequest() {
        viewModelScope.launch {
            state.update { it.copy(dialogVisible = false) }
        }
    }
}