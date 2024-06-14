package com.example.greenifymereloaded.ui.admin.notifications.views

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.data.repository.FormWithAccountName
import com.example.greenifymereloaded.ui.admin.notifications.AdminNotificationViewModel
import com.example.greenifymereloaded.ui.common.shared.SharedAnimatedList
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar
import com.example.greenifymereloaded.ui.common.shared.SharedBackBehavior
import com.example.greenifymereloaded.ui.common.shared.SharedColumn

@Composable
fun AdminNotificationsScreen(model: AdminNotificationViewModel = viewModel(factory = LocalViewModelFactory.current)) {
    val activity = LocalContext.current as Activity
    val state = model.state.collectAsState()
    val tracks = model.formsFlow.collectAsState()

    SharedColumn(applyHorizontalPadding = false) {
        SharedAppBar(
            text = stringResource(R.string.notifications),
            backBehavior = SharedBackBehavior.Enable {
                activity.finish()
            },
        )

        SharedAnimatedList(visible = tracks.value.combinedList.isNotEmpty()) {
            NotificationList(
                forms = tracks.value.combinedList,
                onNotificationClick = { model.onNotificationClicked(it) }
            )
        }
    }

    if (state.value.dialogVisible) {
        ContentDialog(
            item = state.value.selectedNotification!!,
            onDismissRequest = { model.onDismissRequest() },
            setFormViewed = { model.setFormViewedAddPoints() },
            tracks = state.value.tracks
        )
    }
}

@Composable
fun NotificationList(
    forms: List<FormWithAccountName>,
    onNotificationClick: (FormWithAccountName) -> Unit
) {
    LazyColumn(
        modifier = Modifier.clip(
            RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius))
        )
    ) {
        if (forms.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.admin_notification_today),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                )
            }
            items(
                items = forms, key = { it.form.formId }
            ) { item ->
                val type = findCornersType(forms, item)
                NotificationListItem(item, type) { onNotificationClick(item) }
            }
        }
    }
}

private fun findCornersType(
    list: List<FormWithAccountName>,
    item: FormWithAccountName
): CornersType {
    return if (list.size == 1) CornersType.BOTH
    else when (list.indexOf(item)) {
        0 -> CornersType.FIRST
        list.size - 1 -> CornersType.LAST
        else -> CornersType.MIDDLE
    }
}