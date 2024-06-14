package com.example.greenifymereloaded.ui.user_form.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.model.OptionsType
import com.example.greenifymereloaded.ui.user_form.QuantityType


@Composable
fun QuantityForm(
    options: OptionsType,
    isGramsSelected: QuantityType,
    onDialogQuantityChangeSelection: (QuantityType) -> Unit,
    onDialogQuantityQueryChange: (String) -> Unit,
    query: String,
    onEnter: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {

        if (options.pointsPerGram != 0.0 && options.pointsPerPiece != 0.0) {
            SegmentedButtons(
                isGramsSelected = isGramsSelected,
                onDialogQuantityChangeSelection = onDialogQuantityChangeSelection
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            leadingIcon = {
                AnimatedSwitcher(
                    selectedOption = isGramsSelected,
                    gramsContent = {
                        Icon(
                            painter = painterResource(R.drawable.scale),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    piecesContent = {
                        Icon(
                            painter = painterResource(R.drawable.water_bottle),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            },
            onValueChange = onDialogQuantityQueryChange,
            placeholder = {
                AnimatedSwitcher(
                    selectedOption = isGramsSelected,
                    gramsContent = {
                        Text(
                            text = stringResource(R.string.user_dialog_quantity_grams),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    piecesContent = {
                        Text(
                            text = stringResource(R.string.user_dialog_quantity_pieces),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            },
            shape = RoundedCornerShape(dimensionResource(R.dimen.column_card_corner_radius)),
            keyboardActions = KeyboardActions(
                onDone = { onEnter() }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
        )


        Text(
            text = when (isGramsSelected) {
                QuantityType.ONLY_GRAMS -> stringResource(
                    R.string.user_dialog_quantity_grams_dis,
                    options.pointsPerGram.toOneDigit()
                )

                QuantityType.ONLY_PIECES -> stringResource(
                    R.string.user_dialog_quantity_pieces_dis,
                    options.pointsPerPiece.toOneDigit()
                )

                QuantityType.BOTH_SHOW_GRAMS -> stringResource(
                    R.string.user_dialog_quantity_grams_dis,
                    options.pointsPerGram.toOneDigit()
                )

                QuantityType.BOTH_SHOW_PIECES -> stringResource(
                    R.string.user_dialog_quantity_pieces_dis,
                    options.pointsPerPiece.toOneDigit()
                )
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SegmentedButtons(
    isGramsSelected: QuantityType,
    onDialogQuantityChangeSelection: (QuantityType) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            onClick = { onDialogQuantityChangeSelection(QuantityType.BOTH_SHOW_GRAMS) },
            selected = (isGramsSelected == QuantityType.BOTH_SHOW_GRAMS),
            modifier = Modifier.height(50.dp),
        ) {
            Text(stringResource(R.string.user_form_dialog_quantity_option_grams))
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            onClick = { onDialogQuantityChangeSelection(QuantityType.BOTH_SHOW_PIECES) },
            selected = isGramsSelected == QuantityType.BOTH_SHOW_PIECES,
            modifier = Modifier.height(50.dp),
        ) {
            Text(stringResource(R.string.user_form_dialog_quantity_option_pieces))
        }
    }
}

@Composable
private fun AnimatedSwitcher(
    selectedOption: QuantityType,
    gramsContent: @Composable (() -> Unit),
    piecesContent: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        selectedOption,
        transitionSpec = {
            scaleIn(
                initialScale = 0.5f,
                animationSpec = tween(300)
            ) + fadeIn(
                animationSpec = tween(300)
            ) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "Switcher FormDialogDestination Values",
        modifier = modifier
    ) { targetDestination ->
        when (targetDestination) {
            QuantityType.ONLY_GRAMS -> gramsContent()
            QuantityType.ONLY_PIECES -> piecesContent()
            QuantityType.BOTH_SHOW_GRAMS -> gramsContent()
            QuantityType.BOTH_SHOW_PIECES -> piecesContent()
        }
    }
}

fun Double.toOneDigit(): Double {
    return this.toBigDecimal().setScale(1, java.math.RoundingMode.HALF_UP).toDouble()
}