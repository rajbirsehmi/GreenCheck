package com.creative.greencheck.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.greencheck.data.local.FeatureType
import com.creative.greencheck.data.local.UsageManager
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    private val usageManager: UsageManager
) : ViewModel() {

    private val _quotaExhausted = MutableStateFlow<FeatureType?>(null)
    val quotaExhausted = _quotaExhausted.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val remainingProductSearches: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SEARCH_PRODUCT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SEARCH_PRODUCT.limit)

    val remainingIngredientSearches: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SEARCH_INGREDIENT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SEARCH_INGREDIENT.limit)

    fun onSearchProduct(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            if (usageManager.canUseFeature(FeatureType.SEARCH_PRODUCT)) {
                _isLoading.value = true
                _error.value = null
                _searchResults.value = emptyList() // Clear previous results
                val result = repository.searchProducts(query)
                result.fold(
                    onSuccess = { products ->
                        _searchResults.value = products
                        usageManager.incrementUsage(FeatureType.SEARCH_PRODUCT)
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Searching too frequently. Please wait a moment and try again later."
                    }
                )
                _isLoading.value = false
            } else {
                _quotaExhausted.value = FeatureType.SEARCH_PRODUCT
            }
        }
    }

    fun onSearchIngredient(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            if (usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT)) {
                _isLoading.value = true
                _error.value = null
                _searchResults.value = emptyList() // Clear previous results
                val result = repository.searchByIngredient(query)
                result.fold(
                    onSuccess = { products ->
                        _searchResults.value = products
                        usageManager.incrementUsage(FeatureType.SEARCH_INGREDIENT)
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Searching too frequently. Please wait a moment and try again later."
                    }
                )
                _isLoading.value = false
            } else {
                _quotaExhausted.value = FeatureType.SEARCH_INGREDIENT
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
