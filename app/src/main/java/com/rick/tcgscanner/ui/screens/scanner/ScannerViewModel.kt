package com.rick.tcgscanner.ui.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rick.tcgscanner.data.model.Card
import com.rick.tcgscanner.data.model.ScanResult
import com.rick.tcgscanner.data.repository.CardRepository
import com.rick.tcgscanner.data.repository.SettingsRepository
import com.rick.tcgscanner.utils.ImageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed interface ScannerUiState {
    data object Idle : ScannerUiState
    data object Scanning : ScannerUiState
    data class Results(val scan: ScanResult, val cards: List<Card>) : ScannerUiState
    data class Error(val message: String) : ScannerUiState
}

class ScannerViewModel(
    private val cardRepository: CardRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun onPhotoCaptured(file: File) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = ScannerUiState.Scanning
            try {
                val apiKey = settingsRepository.getApiKey()
                val bytes = ImageProcessor.compressForVision(file)
                file.delete()

                val identifyResult = cardRepository.identifyCard(bytes, apiKey)
                identifyResult
                    .onSuccess { scan ->
                        val searchResult = cardRepository.searchCards(scan)
                        searchResult
                            .onSuccess { cards ->
                                _uiState.value = ScannerUiState.Results(scan, cards)
                            }
                            .onFailure { error ->
                                _uiState.value = ScannerUiState.Error(
                                    error.message ?: "Search failed"
                                )
                            }
                    }
                    .onFailure { error ->
                        _uiState.value = ScannerUiState.Error(
                            error.message ?: "Identification failed"
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = ScannerUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun reset() {
        _uiState.value = ScannerUiState.Idle
    }
}

@Suppress("UNCHECKED_CAST")
class ScannerViewModelFactory(
    private val cardRepository: CardRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            return ScannerViewModel(cardRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
