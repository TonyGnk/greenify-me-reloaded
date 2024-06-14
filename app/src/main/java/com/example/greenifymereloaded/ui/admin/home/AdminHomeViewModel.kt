package com.example.greenifymereloaded.ui.admin.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenifymereloaded.R
import com.example.greenifymereloaded.data.di.UserPreferences
import com.example.greenifymereloaded.data.model.Account
import com.example.greenifymereloaded.data.model.RecyclingCategory
import com.example.greenifymereloaded.data.repository.AdminDaoRepository
import com.example.greenifymereloaded.data.repository.UserRepository
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.enums.EnumEntries

class AdminHomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
    private val adminDaoRepository: AdminDaoRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            delay(300)
            state.update {
                it.copy(greetingText = R.string.app_name)
            }
        }
        // cityLevelInit()
        initializeRankChart()
        initializeTotalPoints()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            userPreferences.clear()
        }
    }

    val accountOrderByPoints: StateFlow<List<Account>> =
        adminDaoRepository.getAccountsOrderByPoints().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val state = MutableStateFlow(AdminHomeState())

    val levelState: MutableStateFlow<CityLevelStep> = MutableStateFlow(CityLevelStep(0))

    val rankWinnersItemList: MutableStateFlow<List<Pair<String, Int>>> = MutableStateFlow(listOf())

    val animatedCityLevelInt: MutableStateFlow<Int> = MutableStateFlow(0)
    val animatedCityLevel: MutableStateFlow<Float> = MutableStateFlow(0f)

    val label: Int = R.string.admin_quantity_chart_label
    val rankLabel: Int = R.string.admin_rank_chart_label

    val chartProducerState = MutableStateFlow(CartesianChartModelProducer.build())
    val chartRankProducerState = MutableStateFlow(CartesianChartModelProducer.build())

    val categoryPointsList: MutableStateFlow<Map<RecyclingCategory, Int>> =
        MutableStateFlow(mapOf())


    val pieState = MutableStateFlow(PieState())


    private fun initializeRankChart() = viewModelScope.launch {
        adminDaoRepository.getTop3Accounts().collect { items ->
            if (items.isNotEmpty()) {
                val series = items.map { it.second }.toMutableList()
                if (series.size >= 2) {
                    val temp = series[0]
                    series[0] = series[1]
                    series[1] = temp
                }

                rankWinnersItemList.value = items

                withContext(Dispatchers.Default) {
                    chartRankProducerState.value.tryRunTransaction {
                        columnSeries {
                            series(series)
                        }
                    }
                }
            }
        }
    }

    fun onCategorySelectedPieChart(category: RecyclingCategory) = viewModelScope.launch {

        val totalPointsOfCategory = totalCategoryState.value[category] ?: 0

        //Calculate the percentage of each material in the selected category
        val categoryMaterialsPercent: List<Pair<String, Float>> = materialList.value
            .filter { it.second == category }
            .map { Pair(it.first, it.third.toFloat() / totalPointsOfCategory) }
        pieState.update {
            it.copy(
                percentOfMaterials = categoryMaterialsPercent
            )
        }

        setPieChartDialog(false)

    }


    private fun initializeTotalPoints() = viewModelScope.launch {
        val list: List<Triple<String, RecyclingCategory, Int>> =
            adminDaoRepository.getMaterialTotal()

        if (list.isNotEmpty()) withContext(Dispatchers.Default) {

            val totalCategory: Map<RecyclingCategory, Int> = list.groupBy { it.second }
                .mapValues { (_, list) -> list.sumOf { it.third } }
            totalCategoryState.value = totalCategory

            //For Quantity Chart
            categoryPointsList.update { totalCategory }
            chartProducerState.value.tryRunTransaction {
                columnSeries {
                    series(totalCategory.map { it.value })
                }
            }


            //For Pie Chart
            val selectedCategory = pieState.value.selectedCategory
            val totalPointsOfCategory = totalCategory[selectedCategory] ?: 0

            //Calculate the percentage of each material in the selected category
            val categoryMaterialsPercent: List<Pair<String, Float>> = list
                .filter { it.second == selectedCategory }
                .map { Pair(it.first, it.third.toFloat() / totalPointsOfCategory) }
            pieState.update {
                it.copy(
                    percentOfMaterials = categoryMaterialsPercent
                )
            }

            //For Total Quantity
            val totalQuantity = totalCategory.values.sum()
            levelState.update { CityLevelStep(totalQuantity) }
            animatedCityLevel.update { levelState.value.percentInLevel }
            animatedCityLevelInt.update { levelState.value.pointsInLevel }

            materialList.value = list

        }
    }

    private val materialList: MutableStateFlow<List<Triple<String, RecyclingCategory, Int>>> =
        MutableStateFlow(listOf())

    private val totalCategoryState: MutableStateFlow<Map<RecyclingCategory, Int>> =
        MutableStateFlow(mapOf())

    fun setPieChartDialog(value: Boolean) {
        pieState.update {
            it.copy(
                dialogOpened = value
            )
        }
    }
}


data class PieState(
    val selectedCategory: RecyclingCategory = RecyclingCategory.PLASTIC,
    val dialogOpened: Boolean = false,
    val recyclingCategories: EnumEntries<RecyclingCategory> = RecyclingCategory.entries,
    val percentOfMaterials: List<Pair<String, Float>> = listOf()
)

data class AdminHomeState(
    val greetingText: Int = R.string.empty,
)