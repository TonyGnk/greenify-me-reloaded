package com.example.greenifymereloaded.ui.admin.notifications.views

import com.example.greenifymereloaded.data.repository.FormWithAccountName
import com.example.greenifymereloaded.data.repository.TracksWithMaterial

data class AdminNotificationState2(
    val selectedNotification: FormWithAccountName? = null,
    val tracks: List<TracksWithMaterial> = listOf(),
    val dialogVisible: Boolean = false,
)

data class AdminNotificationState(
    val forms: List<FormWithAccountName> = emptyList(),
) {
    val combinedList = forms.sortedByDescending {
        it.form.createdAt
    }//.take(30)
}