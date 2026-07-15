package com.rick.tcgscanner.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rick.tcgscanner.data.model.PortfolioItem
import com.rick.tcgscanner.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {

    val portfolio: StateFlow<List<PortfolioItem>> = portfolioRepository.portfolio
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearch(query: String) {
        _searchQuery.value = query
    }

    fun delete(item: PortfolioItem) {
        viewModelScope.launch {
            portfolioRepository.delete(item)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class PortfolioViewModelFactory(
    private val portfolioRepository: PortfolioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            return PortfolioViewModel(portfolioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
