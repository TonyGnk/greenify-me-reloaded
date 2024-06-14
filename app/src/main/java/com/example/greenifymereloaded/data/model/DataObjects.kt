package com.example.greenifymereloaded.data.model

import com.example.greenifymereloaded.R
import kotlinx.serialization.Serializable

sealed class DataObject

data class Account(
    val id: String,
    val points: Int,
    val name: String
) : DataObject()


data class Form(
    val formId: String = "0",
    val accountId: String = "0",
    val hasAdminViewed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
) : DataObject()


data class Track(
    val trackId: String = "0",
    val formId: String = "0",
    val materialId: String = "0",
    val quantity: Double = 0.0
) : DataObject()

data class CategoryQuantitySum(
    val category: RecyclingCategory,
    val totalQuantity: Int
)

@Serializable
data class Material(
    val materialId: String = "0",
    val category: RecyclingCategory = RecyclingCategory.OTHER,
    val name: String = "",
    var type: OptionsType = OptionsType(0.0)
) : DataObject()


@Serializable
data class OptionsType(
    val pointsPerPiece: Double = 0.0,
    val pointsPerGram: Double = 0.0
)

enum class RecyclingCategory(val description: Int, val icon: Int) {
    PAPER_CARDBOARD(R.string.recycling_category_paper, R.drawable.box_open),
    PLASTIC(R.string.recycling_category_plastic, R.drawable.bin_bottles),
    METAL_CANS(R.string.recycling_category_metal_cans, R.drawable.can_food),
    ELECTRONICS(R.string.recycling_category_electronics, R.drawable.washer),
    ORGANIC_WASTE(R.string.recycling_category_organic, R.drawable.apple_core),
    GLASS(R.string.recycling_category_glass, R.drawable.glass),
    FABRIC(R.string.recycling_category_fabric, R.drawable.shirt_long_sleeve),
    HAZARDOUS_WASTE(R.string.recycling_category_hazardous, R.drawable.shield_exclamation),
    OTHER(R.string.recycling_category_other, R.drawable.seal_question)
}