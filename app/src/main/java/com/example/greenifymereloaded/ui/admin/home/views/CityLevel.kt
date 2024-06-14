package com.example.greenifymereloaded.ui.admin.home.views

import android.content.Intent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.ui.admin.AdminLevelActivity
import com.example.greenifymereloaded.ui.admin.home.CityLevelStep
import com.example.greenifymereloaded.ui.common.shared.SharedAppBarType
import com.example.greenifymereloaded.ui.common.shared.SharedBehavior
import com.example.greenifymereloaded.ui.common.shared.SharedCard

@Composable
@Preview
fun CityLevel(
    levelState: CityLevelStep = CityLevelStep(points = 2500),
    animatedCityLevel: Int = 0,
) {
    val context = LocalContext.current

    val animatedValue by animateIntAsState(
        targetValue = animatedCityLevel,
        animationSpec = tween(
            durationMillis = 800,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    SharedCard(
        height = 140.dp,
        topBarType = SharedAppBarType.Enable(
            stringResource(R.string.admin_level_of_city_title)
        ),
        behavior = SharedBehavior.Clickable {
            context.startActivity(
                Intent(context, AdminLevelActivity::class.java)
            )
        },
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(levelState.targetingLevel.levelNameResource),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "${animatedValue}/${levelState.targetPointsInLevel} ${
                        stringResource(
                            R.string.admin_level_of_city_points
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Image(
                painter = painterResource(levelState.targetingLevel.imageResource),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight()
            )
        }

    }
}