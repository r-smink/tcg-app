package com.rick.tcgscanner.ui.screens.carddetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rick.tcgscanner.data.model.Card
import com.rick.tcgscanner.data.model.GradePrices
import com.rick.tcgscanner.data.model.PortfolioItem
import com.rick.tcgscanner.data.repository.CardRepository
import com.rick.tcgscanner.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CardDetailUiState {
    data object Loading : CardDetailUiState
    data class Success(val card: Card) : CardDetailUiState
    data class Error(val message: String) : CardDetailUiState
}

class CardDetailViewModel(
    private val cardRepository: CardRepository,
    private val portfolioRepository: PortfolioRepository,
    private val cardId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<CardDetailUiState>(CardDetailUiState.Loading)
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

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
        loadCard()
    }

    private fun loadCard() {
        viewModelScope.launch {
            cardRepository.getCard(cardId)
                .onSuccess { card ->
                    _uiState.value = CardDetailUiState.Success(card)
                    val market = card.tcgplayerPrices?.let { prices ->
                        prices.holofoil?.market
                            ?: prices.reverseHolofoil?.market
                            ?: prices.normal?.market
                    } ?: 0f
                    gradePrices = GradePrices(ungraded = market)
                }
                .onFailure { error ->
                    _uiState.value = CardDetailUiState.Error(
                        error.message ?: "Failed to load card"
                    )
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

    fun saveToPortfolio(onComplete: () -> Unit) {
        val state = _uiState.value
        if (state !is CardDetailUiState.Success) return

        viewModelScope.launch {
            val item = PortfolioItem(
                card = state.card,
                quantity = quantity,
                condition = condition,
                purchasePrice = purchasePrice.toFloatOrNull() ?: 0f,
                notes = notes,
                gradePrices = gradePrices
            )
            portfolioRepository.save(item)
            onComplete()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CardDetailViewModelFactory(
    private val cardRepository: CardRepository,
    private val portfolioRepository: PortfolioRepository,
    private val cardId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailViewModel::class.java)) {
            return CardDetailViewModel(cardRepository, portfolioRepository, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
