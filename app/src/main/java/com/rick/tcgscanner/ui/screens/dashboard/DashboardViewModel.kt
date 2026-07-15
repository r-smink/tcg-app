package com.rick.tcgscanner.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rick.tcgscanner.data.model.PortfolioItem
import com.rick.tcgscanner.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val totalValue: Float = 0f,
    val totalCards: Int = 0,
    val uniqueCards: Int = 0,
    val distributionByRarity: Map<String, Int> = emptyMap(),
    val distributionBySet: Map<String, Int> = emptyMap()
)

class DashboardViewModel(portfolioRepository: PortfolioRepository) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = portfolioRepository.portfolio
        .map { computeDashboard(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    private fun computeDashboard(items: List<PortfolioItem>): DashboardUiState {
        val totalValue = items.sumOf { (it.gradePrices.ungraded * it.quantity).toDouble() }.toFloat()
        val totalCards = items.sumOf { it.quantity }
        val distributionByRarity = items
            .groupBy { it.card.rarity ?: "Unknown" }
            .mapValues { entry -> entry.value.sumOf { it.quantity } }
        val distributionBySet = items
            .groupBy { it.card.setName ?: "Unknown" }
            .mapValues { entry -> entry.value.sumOf { it.quantity } }
        return DashboardUiState(
            totalValue = totalValue,
            totalCards = totalCards,
            uniqueCards = items.size,
            distributionByRarity = distributionByRarity,
            distributionBySet = distributionBySet
        )
    }
}

@Suppress("UNCHECKED_CAST")
class DashboardViewModelFactory(
    private val portfolioRepository: PortfolioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(portfolioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
