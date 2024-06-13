package com.example.greenifymereloaded.ui.user_home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.ui.common.shared.SharedAppBarType
import com.example.greenifymereloaded.ui.common.shared.SharedBehavior
import com.example.greenifymereloaded.ui.common.shared.SharedCard
import com.example.greenifymereloaded.ui.common.shared.SharedProgressBar
import com.example.greenifymereloaded.ui.user_home.UserLevel


@Composable
fun CitizenPoints(
    points: Int,
    pointsInLevel: Int,
    targetPointsInLevel: Int,
    targetingLevel: UserLevel,
    animatedPercentInLevel: Float
) {
    SharedCard(
        topBarType = SharedAppBarType.Enable("Points"),
        applyHorizontalPadding = false,
        behavior = SharedBehavior.Clickable(onClick = {})
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp))
            {
                Text(
                    text = points.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "${pointsInLevel}/${targetPointsInLevel} points",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            SharedProgressBar(
                percent = animatedPercentInLevel,
                startingLabel = (targetingLevel.order - 1).toString(),
                endingLabel = targetingLevel.order.toString(),
            )
        }
    }
}