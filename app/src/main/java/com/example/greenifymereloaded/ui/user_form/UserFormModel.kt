package com.example.greenifymereloaded.ui.user_form

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.model.Material
import com.example.greenifymereloaded.data.model.RecyclingCategory
import com.example.greenifymereloaded.data.model.Track
import com.example.greenifymereloaded.data.repository.UserDaoRepository
import com.example.greenifymereloaded.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class UserFormModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
    private val userDaoRepository: UserDaoRepository
) : ViewModel() {

    val state = MutableStateFlow(UserFormState())


    init {
        viewModelScope.launch {
            userPreferences.userId.collect { id ->
                println("User id: $id")
                if (id.isBlank()) return@collect
                state.update { state ->
                    state.copy(
                        form = state.form.copy(
                            accountId = userDaoRepository.getAccount(id)?.id ?: "1"
                        )
                    )
                }
            }
        }
    }

    fun quitForm(activity: Activity) = activity.finish()

    fun submitForm(activity: Activity) {
        viewModelScope.launch {
            userPreferences.loginUserName.collect { name ->
                Log.d("UserFormModel", "submitForm: $name")
                userDaoRepository.saveFormAndTracks(
                    form = state.value.form,
                    trackList = state.value.trackMaterialsMap.map { it.first },
                    name = name
                )
                activity.finish()
            }

        }
    }

    fun onCategorySelected(category: RecyclingCategory) {
        viewModelScope.launch {
            state.update {
                it.copy(
                    materials = userDaoRepository.getMaterialsWithCategory(category.toString()),
                    selectedCategory = category,
                    dialogDestination = FormDialogDestination.MATERIAL
                )
            }

        }
    }


    fun selectMaterial(material: Material) {
        state.update {
            it.copy(
                selectedMaterial = material,
                dialogDestination = FormDialogDestination.QUANTITY,
                isGramsSelected = if (material.type.pointsPerGram != 0.0 && material.type.pointsPerPiece != 0.0) {
                    QuantityType.BOTH_SHOW_GRAMS
                } else if (material.type.pointsPerGram != 0.0) {
                    QuantityType.ONLY_GRAMS
                } else {
                    QuantityType.ONLY_PIECES
                }
            )
        }
    }

    fun setDialog(value: Boolean) {
        state.update {
            it.copy(
                showDialog = value,
                dialogDestination = FormDialogDestination.CATEGORY
            )
        }
    }

    fun addTrack() {
        val idOfTrack = "2"
        val material = state.value.selectedMaterial
        val selectedMaterial = state.value.selectedMaterial
        val points = when (state.value.isGramsSelected) {
            QuantityType.ONLY_GRAMS -> selectedMaterial.type.pointsPerGram
            QuantityType.ONLY_PIECES -> selectedMaterial.type.pointsPerPiece
            QuantityType.BOTH_SHOW_GRAMS -> selectedMaterial.type.pointsPerGram
            QuantityType.BOTH_SHOW_PIECES -> selectedMaterial.type.pointsPerPiece
        }

        val givenQuantityClean = state.value.query.replace(',', '.')
        val givenQuantity = givenQuantityClean.toFloatOrNull()

        val track = Track(
            formId = idOfTrack,
            materialId = material.materialId,
            quantity = givenQuantity?.times(points) ?: 0.0
        )
        if (givenQuantity != null) state.update {
            it.copy(
                query = "",
                trackToAdd = track,
                trackMaterialsMap = it.trackMaterialsMap + Pair(track, material)
            )
        }
        setDialog(false)
    }

    fun deleteTrack(mutableEntry: Pair<Track, Material>) {
        state.update {
            it.copy(
                trackMaterialsMap = it.trackMaterialsMap.toMutableList().apply {
                    remove(mutableEntry)
                }
            )
        }
    }

    fun onDialogQuantityChangeSelection(type: QuantityType) {
        state.update {
            it.copy(isGramsSelected = type)
        }
    }

    fun onDialogQuantityQueryChange(string: String) {
        state.update {
            it.copy(query = string)
        }
    }

    fun onDismissButton() {
        when (state.value.dialogDestination) {
            FormDialogDestination.CATEGORY -> state.update {
                it.copy(
                    showDialog = false,
                    dialogDestination = FormDialogDestination.CATEGORY
                )
            }

            FormDialogDestination.MATERIAL -> state.update {
                it.copy(dialogDestination = FormDialogDestination.CATEGORY)
            }

            FormDialogDestination.QUANTITY -> state.update {
                it.copy(dialogDestination = FormDialogDestination.MATERIAL)
            }
        }
    }
}