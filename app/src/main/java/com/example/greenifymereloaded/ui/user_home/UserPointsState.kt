package com.example.greenifymereloaded.ui.user_home

import com.example.greenifymereloaded.R

data class UserPointState(
    val points: Int,
    val greetingText: Int = R.string.empty,
    val userId: String = "",
) {
    val targetingLevel: UserLevel = when (points) {
        in 0..<UserLevel1.points -> UserLevel1
        in UserLevel1.points..<UserLevel2.points -> UserLevel2
        else -> UserLevel3
    }
    val pointsInLevel: Int = when (targetingLevel) {
        is UserLevel1 -> points
        is UserLevel2 -> points - UserLevel1.points
        is UserLevel3 -> points - UserLevel2.points
    }
    val targetPointsInLevel: Int = when (targetingLevel) {
        is UserLevel1 -> UserLevel1.points
        is UserLevel2 -> UserLevel2.points - UserLevel1.points
        is UserLevel3 -> UserLevel3.points - UserLevel2.points
    }
    val percentInLevel: Float = pointsInLevel.toFloat() / targetPointsInLevel.toFloat()
    //val listOfLevels: List<UserLevels> = listOf(UserLevel1, UserLevel2, UserLevel3)
}

sealed class UserLevel {
    abstract val order: Int
    abstract val points: Int
    abstract val levelNameResource: Int
}

private data object UserLevel1 : UserLevel() {
    override val order: Int = 1
    override val points: Int = 1000
    override val levelNameResource: Int = R.string.admin_city_level_1
}

private data object UserLevel2 : UserLevel() {
    override val order: Int = 2
    override val points: Int = 2500
    override val levelNameResource: Int = R.string.admin_city_level_2
}

private data object UserLevel3 : UserLevel() {
    override val order: Int = 3
    override val points: Int = 5000
    override val levelNameResource: Int = R.string.admin_city_level_3
}