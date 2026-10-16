package com.creative.greencheck.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.greencheck.data.local.FeatureType
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.domain.repo.Repository
import com.creative.greencheck.util.normalizeBarcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val product: Product) : ProductUiState
    data class Error(val message: String) : ProductUiState
    object Empty : ProductUiState
}

sealed interface AlternativesUiState {
    object Idle : AlternativesUiState
    object Loading : AlternativesUiState
    data class Success(val alternatives: List<Product>) : AlternativesUiState
    data class Error(val message: String) : AlternativesUiState
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var uiState by mutableStateOf<ProductUiState>(ProductUiState.Loading)
        private set

    var alternativesState by mutableStateOf<AlternativesUiState>(AlternativesUiState.Idle)
        private set

    private val _quotaExhausted = MutableStateFlow<FeatureType?>(null)
    val quotaExhausted = _quotaExhausted.asStateFlow()

    private var getProductJob: Job? = null
    private var fetchAlternativesJob: Job? = null
    private var lastAlternativesFetchTime = 0L

    fun getProduct(barcode: String) {
        getProductJob?.cancel()
        val normalizedBarcode = barcode.normalizeBarcode()
        getProductJob = viewModelScope.launch {
            uiState = ProductUiState.Loading
            alternativesState = AlternativesUiState.Idle
            val localProduct = repository.getProductFromDb(normalizedBarcode)
            if (localProduct != null) {
                uiState = ProductUiState.Success(localProduct)
            } else {
                repository.getProduct(normalizedBarcode)
                    .onSuccess { product ->
                        uiState = ProductUiState.Success(product)
                        // Auto-save to database for recents
                        repository.saveProduct(product)
                    }
                    .onFailure {
                        uiState = ProductUiState.Error(it.message ?: "Unknown Error")
                    }
            }
        }
    }

    fun fetchVeganAlternatives(product: Product) {
        val now = System.currentTimeMillis()
        if (now - lastAlternativesFetchTime < 500L && alternativesState is AlternativesUiState.Loading) {
            return
        }
        lastAlternativesFetchTime = now

        fetchAlternativesJob?.cancel()
        fetchAlternativesJob = viewModelScope.launch {
            alternativesState = AlternativesUiState.Loading
            val primaryCategory = product.categories?.split(",")?.firstOrNull()?.trim()
                ?.takeIf { it.isNotBlank() }
            val searchQuery = if (primaryCategory == null) {
                product.name?.split(" ")?.take(2)?.joinToString(" ")
            } else null

            repository.getVeganAlternatives(
                category = primaryCategory,
                searchQuery = searchQuery
            ).onSuccess { alternatives ->
                val productBarcode = product.barcode.normalizeBarcode()
                val filtered = alternatives.filter { it.barcode.normalizeBarcode() != productBarcode }
                alternativesState = if (filtered.isEmpty()) {
                    AlternativesUiState.Error("No vegan alternatives found.")
                } else {
                    AlternativesUiState.Success(filtered)
                }
            }.onFailure {
                alternativesState = AlternativesUiState.Error(it.message ?: "Failed to load alternatives.")
            }
        }
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            repository.saveProduct(product)
        }
    }

    fun resetQuotaState() {
        _quotaExhausted.value = null
    }
}
