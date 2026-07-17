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

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var product by mutableStateOf<Product?>(null)
        private set

    fun getProduct(barcode: String) {
        viewModelScope.launch {
            val result = repository.getProduct(barcode)
            product = result.getOrNull()
        }
    }
}
