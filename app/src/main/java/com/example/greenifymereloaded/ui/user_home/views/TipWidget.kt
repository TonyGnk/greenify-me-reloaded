package com.example.greenifymereloaded.ui.user_home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R

@Composable
@Preview
fun TipWidget() {
    val randomTipResource: Int = listOfTips.random()

    val brush: Brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = brush
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Tip of the Day",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W800,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = stringResource(id = randomTipResource),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

private val listOfTips = listOf(
    R.string.recycling_tip_1,
    R.string.recycling_tip_2,
    R.string.recycling_tip_3,
    R.string.recycling_tip_4,
    R.string.recycling_tip_5,
    R.string.recycling_tip_6,
    R.string.recycling_tip_7,
    R.string.recycling_tip_8,
    R.string.recycling_tip_9,
    R.string.recycling_tip_10,
    R.string.recycling_tip_11,
    R.string.recycling_tip_12,
    R.string.recycling_tip_13,
    R.string.recycling_tip_14,
    R.string.recycling_tip_15,
    R.string.recycling_tip_16,
    R.string.recycling_tip_17,
    R.string.recycling_tip_18,
    R.string.recycling_tip_19,
    R.string.recycling_tip_20,
    R.string.recycling_tip_21,
    R.string.recycling_tip_22,
    R.string.recycling_tip_23,
    R.string.recycling_tip_24,
    R.string.recycling_tip_25,
    R.string.recycling_tip_26,
    R.string.recycling_tip_27,
    R.string.recycling_tip_28,
    R.string.recycling_tip_29,
    R.string.recycling_tip_30,
    R.string.recycling_tip_31,
    R.string.recycling_tip_32,
    R.string.recycling_tip_33,
    R.string.recycling_tip_34,
    R.string.recycling_tip_35,
    R.string.recycling_tip_36,
    R.string.recycling_tip_37,
    R.string.recycling_tip_38,
    R.string.recycling_tip_39,
    R.string.recycling_tip_40
)