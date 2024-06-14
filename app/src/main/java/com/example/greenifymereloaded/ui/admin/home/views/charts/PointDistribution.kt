package com.example.greenifymereloaded.ui.admin.home.views.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.model.RecyclingCategory
import com.example.greenifymereloaded.ui.common.shared.CenteredLargeText
import com.example.greenifymereloaded.ui.common.shared.SharedAppBarType
import com.example.greenifymereloaded.ui.common.shared.SharedCard
import com.example.greenifymereloaded.ui.common.shared.SharedChartCard
import com.example.greenifymereloaded.ui.common.shared.sharedMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.Shape


@Composable
fun PointDistribution(
    quantityLabel: String,
    chartProducerState: CartesianChartModelProducer,
    categoryPointsList: Map<RecyclingCategory, Int>
) {
    SharedCard(
        topBarType = SharedAppBarType.Enable(quantityLabel),
        height = 276.dp
    ) {
        SharedChartCard { Chart(chartProducerState, categoryPointsList) }
    }
}

@Composable
private fun Chart(
    chartProducerState: CartesianChartModelProducer,
    categoryPointsList: Map<RecyclingCategory, Int>
) {
    val categories = categoryPointsList.map { stringResource(it.key.description) }

    val bottomAxisFormatter = CartesianValueFormatter { x, _, _ ->
        if (categories.isNotEmpty()) categories[x.toInt()]
        else x.toString()
    }

    if (categories.isEmpty()) {
        CenteredLargeText(stringResource(R.string.admin_rank_chart_make_a_form))
    } else CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    listOf(MaterialTheme.colorScheme.primary).map {
                        rememberLineComponent(
                            color = it,
                            thickness = 30.dp,
                            shape = Shape.rounded(topLeftDp = 16f, topRightDp = 16f)
                        )
                    },
                ),
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                guideline = rememberLineComponent(Color.Transparent),
                valueFormatter = bottomAxisFormatter
            ),
        ),
        modelProducer = chartProducerState, // Provides the chart data
        marker = sharedMarker(), // Sets the marker style
        runInitialAnimation = true,
        zoomState = rememberVicoZoomState(zoomEnabled = true),
    )

}