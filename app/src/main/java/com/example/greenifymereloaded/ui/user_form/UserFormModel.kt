package com.example.greenifymereloaded.ui.user_form

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.data.model.Both
import com.example.greenifymereloaded.data.model.Form
import com.example.greenifymereloaded.data.model.Grams
import com.example.greenifymereloaded.data.model.Material
import com.example.greenifymereloaded.data.model.Pieces
import com.example.greenifymereloaded.data.model.RecyclingCategory
import com.example.greenifymereloaded.data.model.Track
import com.example.greenifymereloaded.data.repository.UserDaoRepository
import com.example.greenifymereloaded.data.repository.UserRepository
import com.example.greenifymereloaded.ui.user_home.UserPointState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


class UserFormModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
    private val userDaoRepository: UserDaoRepository
) : ViewModel() {

    val state = MutableStateFlow(UserFormState())

    private val _account = MutableStateFlow(Account("1", 0))


    init {
        viewModelScope.launch {
            userPreferences.userId.collect {
                println("User id: $it")
                if (it.isBlank()) return@collect
                _account.value = userDaoRepository.getAccount(it) ?: Account("1", 0)
//                _userHomeState.update { state ->
//                    state.copy(
//                        points = _account.value.points
//                    )
//                }
            }
        }
    }

    fun quitForm(activity: Activity) = activity.finish()

    fun submitForm(activity: Activity) {
        if (!state.value.askPermission) {
            state.update { it.copy(askPermission = true) }
        } else {
//            val notificationHandler = NotificationHandler(activity)
//            val text = activity.getString(R.string.user_submit_form, account.name)
//            val formNotification = NotificationItem.FormNotification(
//                createdAt = form.value.createdAt,
//                formId = form.value.formId,
//                hasViewed = form.value.hasAdminViewed,
//                accountName = account.name,
//                accountId = account.accountId
//            )
            //notificationHandler.showNewFormNotification(text, useSampleData, formNotification)

            //repository.insert(form.value, viewModelScope)
//            state.value.trackMaterialsMap.forEach {
//                repository.insert(it.first, viewModelScope)
//            }
            activity.finish()
        }
    }

    fun onCategorySelected(category: RecyclingCategory) {
        viewModelScope.launch {
            //userDaoRepository.getMaterialsWithCategory(category.toString()).collect
            //  { items ->
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
                isGramsSelected = when (material.type) {
                    is Both -> QuantityType.BOTH_SHOW_GRAMS
                    is Grams -> QuantityType.ONLY_GRAMS
                    is Pieces -> QuantityType.ONLY_PIECES
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
            //Multiply by points to get the total points
            quantity = givenQuantity?.times(points) ?: 0.0
        )
        //if field is not empty
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
        //repository.delete(mutableEntry.first, viewModelScope)
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