package com.example.greenifymereloaded.ui.user_form.views


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.ui.user_form.FormDialogDestination
import com.example.greenifymereloaded.ui.user_form.UserFormModel
import com.example.greenifymereloaded.ui.user_form.UserFormState


/**
 * This composable function is responsible for displaying the main dialog of the user form.
 *
 * @param model The UserFormModel which contains the business logic for the user form.
 * @param state The UserFormState which contains the current state of the user form.
 */
@Composable
fun UserFormDialogMain(model: UserFormModel, state: UserFormState) {

    AlertDialog(
        title = {
            Row {
                Text(stringResource(state.strings.dialogSelect))
                AnimatedDialogSwitcher(
                    state.dialogDestination,
                    { Text(stringResource(FormDialogDestination.CATEGORY.title)) },
                    { Text(stringResource(FormDialogDestination.MATERIAL.title)) },
                    { Text(stringResource(FormDialogDestination.QUANTITY.title)) }
                )
            }
        },
        text = {
            AnimatedDialogSwitcher(
                state.dialogDestination,
                {
                    CategoriesGrid({ model.onCategorySelected(it) }, state.recyclingCategories)
                },
                {
                    MaterialsList(model, state)
                },
                {
//                    QuantityForm(
//                        options = state.selectedMaterial.type,
//                        isGramsSelected = state.isGramsSelected,
//                        onDialogQuantityChangeSelection = {
//                            model.onDialogQuantityChangeSelection(
//                                it
//                            )
//                        },
//                        onDialogQuantityQueryChange = { model.onDialogQuantityQueryChange(it) },
//                        query = state.query,
//                        onEnter = { model.addTrack() }
//                    )
                },
                modifier = Modifier.height(300.dp)
            )
        },
        dismissButton = {
            TextButton(onClick = { model.onDismissButton() })
            {
                AnimatedDialogSwitcher(
                    state.dialogDestination,
                    { Text(stringResource(state.strings.dialogCancel)) },
                    { Text(stringResource(state.strings.dialogBack)) },
                    { Text(stringResource(state.strings.dialogBack)) }
                )
            }
        },
        confirmButton = {
            AnimatedDialogSwitcher(
                state.dialogDestination,
                { }, // Don't show when adding a category
                { },
                {
                    Button(onClick = { model.addTrack() })
                    {
                        Text(stringResource(state.strings.dialogAdd))
                    }
                }
            )
        },
        onDismissRequest = { model.setDialog(false) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp,
        modifier = Modifier.width(406.dp)
    )
}