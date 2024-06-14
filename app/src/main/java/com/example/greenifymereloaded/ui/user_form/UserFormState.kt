package com.example.greenifymereloaded.ui.user_form

import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.model.Form
import com.example.greenifymereloaded.data.model.Material
import com.example.greenifymereloaded.data.model.RecyclingCategory
import com.example.greenifymereloaded.data.model.Track
import kotlin.enums.EnumEntries

data class UserFormState(
    val form: Form = Form(),
    val materials: List<Material> = listOf(),
    val trackToAdd: Track? = null,
    val trackMaterialsMap: List<Pair<Track, Material>> = listOf(),
    val selectedMaterial: Material = Material(
        materialId = "0",
        category = RecyclingCategory.OTHER,
        name = ""
    ),
    val isGramsSelected: QuantityType = QuantityType.ONLY_GRAMS,
    val query: String = "",
    val recyclingCategories: EnumEntries<RecyclingCategory> = RecyclingCategory.entries,
    val selectedCategory: RecyclingCategory = RecyclingCategory.PLASTIC,
    val showDialog: Boolean = true,
    val dialogDestination: FormDialogDestination = FormDialogDestination.CATEGORY,
    val strings: UserFormStrings = UserFormStrings()
)

data class UserFormStrings(
    val appBarLabel: Int = R.string.user_form_app_bar_label,
    val appBarCancel: Int = R.string.user_form_app_bar_cancel,
    val actionButtonsAdd: Int = R.string.user_form_action_buttons_add,
    val dialogCancel: Int = R.string.user_form_dialog_cancel,
    val dialogBack: Int = R.string.user_form_dialog_back,
    val dialogAdd: Int = R.string.user_form_dialog_add,
    val dialogSelect: Int = R.string.user_form_dialog_select
)

enum class FormDialogDestination(val title: Int) {
    CATEGORY(R.string.user_form_dialog_category_title),
    MATERIAL(R.string.user_form_dialog_material_title),
    QUANTITY(R.string.user_form_dialog_quantity_title)
}

enum class QuantityType {
    ONLY_GRAMS, ONLY_PIECES, BOTH_SHOW_GRAMS, BOTH_SHOW_PIECES
}