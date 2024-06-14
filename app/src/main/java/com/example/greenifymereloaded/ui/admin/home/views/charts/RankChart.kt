package com.example.greenifymereloaded.ui.admin.home.views.charts

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.ui.common.shared.RankActivity
import com.example.greenifymereloaded.ui.common.shared.SharedAppBarType
import com.example.greenifymereloaded.ui.common.shared.SharedBehavior
import com.example.greenifymereloaded.ui.common.shared.SharedCard
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer


@Composable
fun RankChart(
    producer: CartesianChartModelProducer,
    winnersItemList: List<Pair<String, Int>>,
    label: String,
) {
    val context = LocalContext.current
    SharedCard(
        height = 246.dp, //DO NOT CHANGE
        topBarType = SharedAppBarType.Enable(label),
        behavior = if (winnersItemList.isEmpty()) SharedBehavior.Static
        else SharedBehavior.Clickable {
            context.startActivity(
                Intent(context, RankActivity::class.java)
            )
        },
    ) {
        RankChartArea(
            producer, winnersItemList
        )
    }
}