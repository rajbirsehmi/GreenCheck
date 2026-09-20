package com.creative.isitvegan.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.isitvegan.data.local.FeatureType
import com.creative.isitvegan.data.local.UsageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(
    private val usageManager: UsageManager
) : ViewModel() {

    val scannerUsage: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SCANNER)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SCANNER.limit)

    val manualUsage: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.MANUAL_ENTRY)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.MANUAL_ENTRY.limit)

    val productSearchUsage: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SEARCH_PRODUCT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SEARCH_PRODUCT.limit)

    val ingredientSearchUsage: StateFlow<Int> = usageManager.getRemainingUsage(FeatureType.SEARCH_INGREDIENT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureType.SEARCH_INGREDIENT.limit)

    val nextResetTime: StateFlow<Long> = usageManager.getNextResetTimestamp()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val hasSeenIntro: StateFlow<Boolean> = usageManager.hasSeenIntro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setHasSeenIntro(seen: Boolean) {
        viewModelScope.launch {
            usageManager.setHasSeenIntro(seen)
        }
    }
}
