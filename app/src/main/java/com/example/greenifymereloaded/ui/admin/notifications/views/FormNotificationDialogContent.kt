package com.example.greenifymereloaded.ui.admin.notifications.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.repository.FormWithAccountName
import com.example.greenifymereloaded.data.repository.TracksWithMaterial
import com.example.greenifymereloaded.ui.user_form.views.toOneDigit
import java.text.SimpleDateFormat

@Composable
fun FormNotificationDialogContent(
    item: FormWithAccountName,
    tracks: List<TracksWithMaterial>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.admin_notification_form_information),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(14.dp)
        ) {
            Row {
                Icon(
                    painter = painterResource(R.drawable.user),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = item.account.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Row {
                Icon(
                    painter = painterResource(R.drawable.calendar_lines),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = getFullTimeFromEpoch(item.form.createdAt),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Row {
                Icon(
                    painter = painterResource(R.drawable.coins),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = tracks.sumOf { it.track.quantity }.toOneDigit()
                        .toString() + " " + stringResource(R.string.admin_notification_form_points),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "${stringResource(R.string.admin_notification_form_tracks)} (${tracks.size})",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(14.dp)
                .heightIn(max = 160.dp)
        ) {
            items(items = tracks, key = { it.track.trackId }) {
                Row {
                    Icon(
                        painter = painterResource(it.material.category.icon),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = it.material.name,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = it.track.quantity.toOneDigit().toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getFullTimeFromEpoch(date: Long): String {
    return SimpleDateFormat("HH:mm dd/MM/yy").format(date)
}