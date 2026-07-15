package com.rick.tcgscanner.ui.screens.portfolio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rick.tcgscanner.data.model.GradePrices
import com.rick.tcgscanner.data.model.PortfolioItem
import com.rick.tcgscanner.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PortfolioDetailUiState {
    data object Loading : PortfolioDetailUiState
    data class Success(val item: PortfolioItem) : PortfolioDetailUiState
    data class Error(val message: String) : PortfolioDetailUiState
}

class PortfolioDetailViewModel(
    private val portfolioRepository: PortfolioRepository,
    private val portfolioId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<PortfolioDetailUiState>(PortfolioDetailUiState.Loading)
    val uiState: StateFlow<PortfolioDetailUiState> = _uiState.asStateFlow()

    var quantity by mutableIntStateOf(1)
        private set
    var condition by mutableStateOf("Near Mint")
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set
    var gradePrices by mutableStateOf(GradePrices())
        private set

    init {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            val item = portfolioRepository.getItem(portfolioId)
            if (item != null) {
                quantity = item.quantity
                condition = item.condition
                purchasePrice = if (item.purchasePrice == 0f) "" else item.purchasePrice.toString()
                notes = item.notes
                gradePrices = item.gradePrices
                _uiState.value = PortfolioDetailUiState.Success(item)
            } else {
                _uiState.value = PortfolioDetailUiState.Error("Item not found")
            }
        }
    }

    fun updateQuantity(value: String) {
        quantity = value.toIntOrNull()?.coerceAtLeast(1) ?: 1
    }

    fun updateCondition(value: String) {
        condition = value
    }

    fun updatePurchasePrice(value: String) {
        purchasePrice = value
    }

    fun updateNotes(value: String) {
        notes = value
    }

    fun updateGradePrices(value: GradePrices) {
        gradePrices = value
    }

    fun save(onComplete: () -> Unit) {
        val state = _uiState.value
        if (state !is PortfolioDetailUiState.Success) return
        viewModelScope.launch {
            val updated = state.item.copy(
                quantity = quantity,
                condition = condition,
                purchasePrice = purchasePrice.toFloatOrNull() ?: 0f,
                notes = notes,
                gradePrices = gradePrices
            )
            portfolioRepository.save(updated)
            onComplete()
        }
    }

    fun delete(onComplete: () -> Unit) {
        val state = _uiState.value
        if (state !is PortfolioDetailUiState.Success) return
        viewModelScope.launch {
            portfolioRepository.delete(state.item)
            onComplete()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class PortfolioDetailViewModelFactory(
    private val portfolioRepository: PortfolioRepository,
    private val portfolioId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioDetailViewModel::class.java)) {
            return PortfolioDetailViewModel(portfolioRepository, portfolioId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
