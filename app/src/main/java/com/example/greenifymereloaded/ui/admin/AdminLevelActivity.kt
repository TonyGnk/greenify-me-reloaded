package com.example.greenifymereloaded.ui.admin

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenifymereloaded.MyApplication
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.data.di.ProvideViewModelFactory
import com.example.greenifymereloaded.ui.admin.home.AdminHomeViewModel
import com.example.greenifymereloaded.ui.admin.home.CityLevelStep
import com.example.greenifymereloaded.ui.admin.home.Levels
import com.example.greenifymereloaded.ui.common.shared.SharedAppBar
import com.example.greenifymereloaded.ui.common.shared.SharedBackBehavior
import com.example.greenifymereloaded.ui.common.shared.SharedLazyColumn
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme


class AdminLevelActivity : ComponentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ComposeTheme {
                ProvideViewModelFactory(
                    myApplication = LocalContext.current.applicationContext as MyApplication
                ) {
                    AdminLevel()
                }
            }
        }
    }
}

@Composable
private fun AdminLevel(model: AdminHomeViewModel = viewModel(factory = LocalViewModelFactory.current)) {
    val state by model.levelState.collectAsState()
    val animatedCityLevel by model.animatedCityLevel.collectAsState()

    SharedLazyColumn(applyHorizontalPadding = false) {
        item {
            TopBar()
        }
        item {
            ImageCard(state, animatedCityLevel)
        }
        item {
            DescriptionCard(state)
        }
    }
}

@Composable
private fun ImageCard(state: CityLevelStep, animatedState: Float) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)),
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
    ) {
        CircleBar(state, animatedState)
    }

}

@Composable
private fun DescriptionCard(state: CityLevelStep) {

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)),
        modifier = Modifier.fillMaxWidth()
    ) {
        val listOfLevels: List<Levels> = state.listOfLevels
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (level in listOfLevels) {
                LevelListItem(state, level)
                if (listOfLevels.indexOf(level) != listOfLevels.lastIndex) HorizontalDivider()
            }
        }
    }
}

@Composable
private fun LevelListItem(
    state: CityLevelStep,
    level: Levels,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        headlineContent = {
            Text(
                text = stringResource(level.levelNameResource),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        },
        trailingContent = {
            if (level.order < state.targetingLevel.order)
                Icon(
                    painter = painterResource(R.drawable.check_double),
                    contentDescription = "",
                    modifier = Modifier.size(22.dp)
                )
            else if (level.order == state.targetingLevel.order)
                Text(
                    text = "${state.pointsInLevel} / ${state.targetPointsInLevel} points",
                    style = MaterialTheme.typography.titleMedium,
                )
            else
                Text(
                    text = "0 / ${level.points}  ${stringResource(R.string.admin_notification_form_points)}",
                    style = MaterialTheme.typography.titleMedium,
                )

        }
    )
}

@Composable
private fun TopBar() {
    val text = stringResource(R.string.admin_level_of_city_title)
    val activity = LocalContext.current as Activity

    SharedAppBar(
        text = text,
        backBehavior = SharedBackBehavior.Enable { activity.finish() },
    )
}

@Composable
private fun CircleBar(
    state: CityLevelStep, animatedState: Float,
) {
    val offset = 0.001f

    val animatedValue by animateFloatAsState(
        targetValue = animatedState,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ), label = ""
    )


    val gradient = Brush.sweepGradient(
        colorStops = listOf(
            animatedValue to MaterialTheme.colorScheme.primary,
            animatedValue + offset to MaterialTheme.colorScheme.surfaceContainer
        ).toTypedArray(),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .padding(32.dp)
                .rotate(-90f)
                .border(width = 12.dp, brush = gradient, shape = CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLow, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(400.dp)
                .padding(50.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Image(
                    painter = painterResource(state.targetingLevel.imageResource),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                )
                Text(
                    text = stringResource(state.targetingLevel.levelNameResource),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(0.5f)
                        .width(190.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}