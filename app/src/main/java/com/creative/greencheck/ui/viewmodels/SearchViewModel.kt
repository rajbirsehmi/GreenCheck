package com.creative.greencheck.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.greencheck.data.local.FeatureType
import com.creative.greencheck.data.local.UsageManager
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private val _currentQuery = MutableStateFlow("")
    val currentQuery = _currentQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val remainingIngredientSearches: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SEARCH_INGREDIENT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SEARCH_INGREDIENT.limit)

    private var searchJob: Job? = null
    private var lastQuery: String? = null
    private var lastSearchTime = 0L

    fun onSearchProduct(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) return

        _currentQuery.value = trimmedQuery

        val now = System.currentTimeMillis()
        if (trimmedQuery == lastQuery && now - lastSearchTime < 500L && _isLoading.value) {
            return
        }
        lastQuery = trimmedQuery
        lastSearchTime = now

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT)) {
                _isLoading.value = true
                _error.value = null
                _searchResults.value = emptyList() // Clear previous results
                val result = repository.searchProducts(trimmedQuery)
                result.fold(
                    onSuccess = { products ->
                        _searchResults.value = products
                        usageManager.incrementUsage(FeatureType.SEARCH_INGREDIENT)
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Unable to complete search. Please try again."
                    }
                )
                _isLoading.value = false
            } else {
                _quotaExhausted.value = FeatureType.SEARCH_INGREDIENT
            }
        }
    }

    fun onSearchIngredient(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) return

        _currentQuery.value = trimmedQuery

        val now = System.currentTimeMillis()
        if (trimmedQuery == lastQuery && now - lastSearchTime < 500L && _isLoading.value) {
            return
        }
        lastQuery = trimmedQuery
        lastSearchTime = now

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT)) {
                _isLoading.value = true
                _error.value = null
                _searchResults.value = emptyList() // Clear previous results
                val result = repository.searchByIngredient(trimmedQuery)
                result.fold(
                    onSuccess = { products ->
                        _searchResults.value = products
                        usageManager.incrementUsage(FeatureType.SEARCH_INGREDIENT)
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "Unable to complete search. Please try again."
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
