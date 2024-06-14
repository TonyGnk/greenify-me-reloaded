package com.example.greenifymereloaded.ui.admin.home.views

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.ui.admin.home.AdminHomeViewModel
import com.example.greenifymereloaded.ui.admin.home.views.charts.PieChart
import com.example.greenifymereloaded.ui.admin.home.views.charts.PointDistribution
import com.example.greenifymereloaded.ui.admin.home.views.charts.RankChart
import com.example.greenifymereloaded.ui.admin.notifications.AdminNotificationsActivity
import com.example.greenifymereloaded.ui.common.shared.SharedLazyColumn
import com.example.greenifymereloaded.ui.user_form.views.CategoriesGrid
import com.example.greenifymereloaded.ui.user_home.views.TipWidget


@Composable
fun AdminHome(
    navController: NavController,
    model: AdminHomeViewModel = viewModel(factory = LocalViewModelFactory.current)
) {
    val state by model.state.collectAsState()
    val levelState by model.levelState.collectAsState()
    val pieState by model.pieState.collectAsState()
    val chartProducerState by model.chartProducerState.collectAsState()
    val categoryPointsList by model.categoryPointsList.collectAsState()
    val animatedCityLevel by model.animatedCityLevelInt.collectAsState()
    val winnersItemList by model.chartRankProducerState.collectAsState()
    val rankWinnersItemList by model.rankWinnersItemList.collectAsState()
    val context = LocalContext.current as Activity


    SharedLazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            AdminHomeAppBar(
                text = stringResource(state.greetingText),
                onNotificationsIcon = {
                    context.startActivity(
                        Intent(context, AdminNotificationsActivity::class.java)
                    )
                },
                onExit = {
                    model.logout()
                    navController.navigate("login") {
                        popUpTo("admin") { inclusive = true }
                    }
                }
            )
        }
        item {
            TipWidget()
        }
        item {
            CityLevel(levelState, animatedCityLevel)
        }
        item {
            PointDistribution(
                stringResource(model.label), chartProducerState, categoryPointsList
            )
        }
        item {
            RankChart(
                producer = winnersItemList,
                winnersItemList = rankWinnersItemList,
                label = stringResource(model.rankLabel),
            )
        }
        item {
            PieChart(
                listOfPiecesWithNames = pieState.percentOfMaterials,
                label = stringResource(pieState.selectedCategory.description),
                onSelectButtonClicked = { model.setPieChartDialog(true) }
            )
        }
    }

    if (pieState.dialogOpened) {
        PieChartDialog({ model.setPieChartDialog(false) }) {
            CategoriesGrid({ model.onCategorySelectedPieChart(it) }, pieState.recyclingCategories)
        }
    }
}

/**
 * Pie Chart Dialog Composable
 *
 * @param onDismiss Lambda function to handle dismiss action
 * @param content Composable content for the dialog
 */
@Composable
fun PieChartDialog(onDismiss: () -> Unit = {}, content: @Composable ColumnScope.() -> Unit = {}) {
    Dialog(onDismiss) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius))
                )
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.admin_home_pie_select_category),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            content()
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}