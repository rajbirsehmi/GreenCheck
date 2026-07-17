package com.creative.isitvegan.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val product: Product) : ProductUiState
    data class Error(val message: String) : ProductUiState
    object Empty : ProductUiState
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var uiState by mutableStateOf<ProductUiState>(ProductUiState.Loading)
        private set

    fun getProduct(barcode: String) {
        viewModelScope.launch {
            uiState = ProductUiState.Loading
            val localProduct = repository.getProductFromDb(barcode)
            if (localProduct != null) {
                uiState = ProductUiState.Success(localProduct)
            } else {
                repository.getProduct(barcode)
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
}
