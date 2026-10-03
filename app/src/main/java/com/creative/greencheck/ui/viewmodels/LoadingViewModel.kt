package com.creative.greencheck.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.greencheck.data.repo.BarcodeNotFoundException
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoadingUiState {
    object Idle : LoadingUiState
    object Loading : LoadingUiState
    data class Success(val product: Product) : LoadingUiState
    data class Error(val message: String) : LoadingUiState
    data class BarcodeError(val message: String) : LoadingUiState
    data class NetworkError(val message: String) : LoadingUiState
}

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val TAG = "LoadingViewModel"

    var uiState by mutableStateOf<LoadingUiState>(LoadingUiState.Idle)
        private set

    fun getProduct(barcode: String) {
        if (uiState is LoadingUiState.Loading) return

        viewModelScope.launch {
            uiState = LoadingUiState.Loading
            
            // 1. Check Database First
            val localProduct = repository.getProductFromDb(barcode)
            if (localProduct != null) {
                Log.d(TAG, "Database Hit: ${localProduct.name}")
                uiState = LoadingUiState.Success(localProduct)
                return@launch
            }

            // 2. Fallback to API
            Log.d(TAG, "Fetching from API: $barcode")
            repository.getProduct(barcode)
                .onSuccess { product ->
                    Log.d(TAG, "API Success: ${product.name}")
                    try {
                        repository.saveProduct(product)
                    } catch (e: Exception) {
                        Log.e(TAG, "Database Save Error: ${e.message}")
                    }
                    uiState = LoadingUiState.Success(product)
                }
                .onFailure { e ->
                    Log.e(TAG, "API Failure: ${e.message}")
                    val msg = e.message ?: "An unknown error occurred"
                    if (e is BarcodeNotFoundException || msg.contains("status 0") == true || msg.contains("product not found") == true || msg.contains("Product not found") == true) {
                        uiState = LoadingUiState.BarcodeError(msg)
                    } else {
                        uiState = LoadingUiState.NetworkError(msg)
                    }
                }
        }
    }
}
