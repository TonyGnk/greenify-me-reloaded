package com.example.greenifymereloaded.ui.common.shared

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenifymereloaded.MyApplication
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.LocalViewModelFactory
import com.example.greenifymereloaded.data.di.ProvideViewModelFactory
import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.ui.admin.home.AdminHomeViewModel
import com.example.greenifymereloaded.ui.common.theme.ComposeTheme

class RankActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                ProvideViewModelFactory(
                    myApplication = LocalContext.current.applicationContext as MyApplication
                ) {
                    AdminRank()
                }
            }
        }
    }
}


@Composable
private fun AdminRank(model: AdminHomeViewModel = viewModel(factory = LocalViewModelFactory.current)) {
    val accountList by model.accountOrderByPoints.collectAsState()

    SharedColumn(applyHorizontalPadding = false) {
        TopBar(stringResource(model.rankLabel))
        AdminRankGrid(accountList)
    }
}


/**
 * Displays the top app bar with optional back button functionality.
 *
 * @param text The text to display in the app bar.
 */
@Composable
private fun TopBar(text: String = "Rank") {
    val activity = LocalContext.current as Activity

    SharedAppBar(
        text = text,
        backBehavior = SharedBackBehavior.Enable { activity.finish() },
    )
}

/**
 * Displays a grid of admin ranks using a LazyColumn.
 *
 * @param accountList List of accounts to display.
 */
@Composable
fun AdminRankGrid(accountList: List<Account>) {
    SharedAnimatedList(visible = accountList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius))
            ),
            state = rememberLazyListState(initialFirstVisibleItemIndex = 0)
        ) {
            itemsIndexed(items = accountList) { index, item ->
                AccountListItem(
                    item = item,
                    index = index
                )
            }
        }
    }
}

/**
 * Displays an item in the admin rank list.
 *
 * @param item The account to display.
 * @param index The position of the item in the list.
 */
@Composable
private fun AccountListItem(
    item: Account,
    index: Int = 3
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        headlineContent = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.horizontalScreenPadding))
            ) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                when (index) {
                    0 -> Icon(
                        painter = painterResource(R.drawable.trophy_star),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(25.dp)
                    )

                    else -> Text(
                        "#${index + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}