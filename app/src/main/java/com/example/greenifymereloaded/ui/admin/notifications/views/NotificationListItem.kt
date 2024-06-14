package com.example.greenifymereloaded.ui.admin.notifications.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.repository.FormWithAccountName
import java.text.SimpleDateFormat

@Composable
fun NotificationListItem(
    item: FormWithAccountName, type: CornersType, onClick: () -> Unit
) {
    Column {
        ListItem(
            headlineContent = {
                HeadLineText(item)
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.baseline_receipt_long_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(3.dp)
                )
            },
            trailingContent = {
                Text(
                    text = getTimeFromEpoch(item.form.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            modifier = Modifier
                .clip(
                    when (type) {
                        CornersType.FIRST -> RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.column_card_corner_radius),
                            topEnd = dimensionResource(R.dimen.column_card_corner_radius),
                        )

                        CornersType.LAST -> RoundedCornerShape(
                            bottomStart = dimensionResource(R.dimen.column_card_corner_radius),
                            bottomEnd = dimensionResource(R.dimen.column_card_corner_radius),
                        )

                        CornersType.MIDDLE -> RectangleShape
                        CornersType.BOTH -> RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius))
                    }
                )
                .clickable { onClick() }
        )
        if (type != CornersType.LAST && type != CornersType.BOTH) HorizontalDivider(Modifier.fillMaxWidth())

    }
}

@Composable
private fun HeadLineText(item: FormWithAccountName) {
    Text(
        text = stringResource(R.string.user_submit_form, item.account.name),
        fontSize = 14.sp,
    )
}

enum class CornersType { FIRST, MIDDLE, LAST, BOTH }

//Get only the time
@SuppressLint("SimpleDateFormat")
fun getTimeFromEpoch(date: Long): String {
    return SimpleDateFormat("HH:mm").format(date)
}