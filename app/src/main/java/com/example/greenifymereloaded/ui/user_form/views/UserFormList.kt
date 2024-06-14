package com.example.greenifymereloaded.ui.user_form.views


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.model.Material
import com.example.greenifymereloaded.data.model.Track
import com.example.greenifymereloaded.ui.user_form.UserFormModel
import com.example.greenifymereloaded.ui.user_form.UserFormState
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UserFormList(model: UserFormModel, state: UserFormState, modifier: Modifier = Modifier) {
    //val tracks = model.tracksOfForm.collectAsState()
    //val materials = model.materialsForm.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        val map = state.trackMaterialsMap
        items(items = map) {

            TrackItem(it) { model.deleteTrack(it) }
        }
    }
}

@Composable
private fun TrackItem(pair: Pair<Track, Material>, onDelete: () -> Unit) {
    val delete = SwipeAction(
        onSwipe = onDelete,
        icon = {
            Icon(
                painter = painterResource(R.drawable.trash),
                contentDescription = "",
                modifier = Modifier.padding(16.dp)
            )
        },
        background = Color.Transparent,
    )
    SwipeableActionsBox(
        swipeThreshold = 170.dp,
        endActions = listOf(delete),
        backgroundUntilSwipeThreshold = Color.Transparent
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            leadingContent = {
                Icon(
                    painter = painterResource(pair.second.category.icon),
                    contentDescription = "",
                    modifier = Modifier.size(22.dp)
                )
            },
            headlineContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(pair.second.name)
                    Text(pair.first.quantity.toString())
                }
            },
            modifier = Modifier.clip(CircleShape)
        )
    }
}